package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.dto.SaleDTOBuilder;
import com.gabrielspassos.poc.builder.entity.SaleEntityBuilder;
import com.gabrielspassos.poc.client.kafka.JudicialProducer;
import com.gabrielspassos.poc.client.kafka.PersonProducer;
import com.gabrielspassos.poc.config.SaleConfig;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.PersonJudicialValidationDTO;
import com.gabrielspassos.poc.dto.PersonValidationDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.entity.SaleEntity;
import com.gabrielspassos.poc.enumerator.PersonJudicialValidationStatusEnum;
import com.gabrielspassos.poc.enumerator.PersonValidationStatusEnum;
import com.gabrielspassos.poc.enumerator.SaleStatusEnum;
import com.gabrielspassos.poc.exception.PersonSaleValidationException;
import com.gabrielspassos.poc.exception.ScoreSaleValidationException;
import com.gabrielspassos.poc.repository.SaleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

@Slf4j
@Service
@AllArgsConstructor
public class SaleService {

    private final PersonValidationService personValidationService;
    private final PersonJudicialValidationService personJudicialValidationService;
    private final PersonScoreService personScoreService;
    private final SaleRepository saleRepository;
    private final PersonProducer personProducer;
    private final JudicialProducer judicialProducer;
    private final SaleConfig saleConfig;

    public Mono<SaleDTO> createNewSale(PersonDTO personDTO) {
        SaleEntity saleEntity = SaleEntityBuilder.build(personDTO);

        return saleRepository.save(saleEntity)
                .flatMap(this::sendSaleEvent);
    }

    public Flux<SaleDTO> analyzeSalesScores() {
        return saleRepository.findByStatus(SaleStatusEnum.LEAD)
                .map(SaleDTOBuilder::build)
                .flatMap(this::getPersonValidations)
                .flatMap(this::updateSalesStatus);
    }

    private Mono<SaleDTO> sendSaleEvent(SaleEntity saleEntity) {
        SaleDTO saleDTO = SaleDTOBuilder.build(saleEntity);

        return Mono.zip(personProducer.sendVoteToTopic(saleDTO), judicialProducer.sendVoteToTopic(saleDTO))
                .map(tuple -> saleDTO);
    }

    private Mono<Tuple3<SaleDTO, PersonValidationDTO, PersonJudicialValidationDTO>> getPersonValidations(SaleDTO saleDTO) {
        String saleId = saleDTO.getId();

        return Mono.zip(
                personValidationService.getPersonValidationBySaleId(saleId),
                personJudicialValidationService.getJudicialValidationBySaleId(saleId)
        ).map(tuple -> Tuples.of(saleDTO, tuple.getT1(), tuple.getT2()));
    }

    private Flux<SaleDTO> updateSalesStatus(Tuple3<SaleDTO, PersonValidationDTO, PersonJudicialValidationDTO> tuple3) {
        SaleDTO saleDTO = tuple3.getT1();

        return Flux.just(tuple3)
                .filter(tuple -> {
                    return PersonValidationStatusEnum.APPROVED.equals(tuple.getT2().getStatus())
                            && PersonJudicialValidationStatusEnum.APPROVED.equals(tuple.getT3().getStatus());
                }).switchIfEmpty(Flux.error(new PersonSaleValidationException()))
                .flatMap(tuple -> personScoreService.createPersonScore(tuple.getT1())
                        .map(personScoreDTO -> Tuples.of(tuple.getT1(), personScoreDTO)))
                .filter(tuple -> tuple.getT2().getScore() >= saleConfig.getMinimumValidScore())
                .switchIfEmpty(Flux.error(new ScoreSaleValidationException()))
                .map(tuple -> SaleEntityBuilder.build(tuple.getT1(), SaleStatusEnum.PROSPECT))
                .flatMap(saleRepository::save)
                .map(SaleDTOBuilder::build)
                .onErrorResume(throwable -> saveErrorSale(saleDTO));
    }

    private Mono<SaleDTO> saveErrorSale(SaleDTO saleDTO) {
        SaleEntity saleEntity = SaleEntityBuilder.build(saleDTO, SaleStatusEnum.REJECT);
        return saleRepository.save(saleEntity)
                .map(SaleDTOBuilder::build)
                .doOnSuccess(dto -> log.info("Saldo vendo rejeitada {}", dto));
    }
}
