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

    public Mono<SaleEntity> createNewSale(PersonDTO personDTO) {
        SaleEntity saleEntity = SaleEntityBuilder.build(personDTO);

        return saleRepository.save(saleEntity)
                .flatMap(this::sendSaleEvent);
    }

    public Mono<SaleEntity> getSaleById(String id) {
        return saleRepository.findById(id)
                .doOnSuccess(saleEntity -> log.info("Encontrado venda {}", saleEntity));
    }

    public Flux<SaleDTO> analyzeSalesScores() {
        return saleRepository.findByStatus(SaleStatusEnum.LEAD)
                .map(SaleDTOBuilder::build)
                .flatMap(this::getPersonValidations)
                .filter(tuple -> {
                    return PersonValidationStatusEnum.APPROVED.equals(tuple.getT2().getStatus())
                            && PersonJudicialValidationStatusEnum.APPROVED.equals(tuple.getT3().getStatus());
                }).switchIfEmpty(Flux.error(new RuntimeException())) //todo: rever
                .flatMap(tuple -> personScoreService.savePersonScore(tuple.getT1())
                        .map(personScoreDTO -> Tuples.of(tuple.getT1(), personScoreDTO)))
                .filter(tuple -> saleConfig.getMinimumValidScore() >= tuple.getT2().getScore())
                .switchIfEmpty(Flux.error(new RuntimeException()))
                .map(tuple -> SaleEntityBuilder.build(tuple.getT1(), SaleStatusEnum.PROSPECT))
                .flatMap(saleRepository::save)
                .map(SaleDTOBuilder::build);
    }

    private Mono<SaleEntity> sendSaleEvent(SaleEntity saleEntity) {
        String saleId = saleEntity.getId();

        return Mono.zip(personProducer.sendVoteToTopic(saleId), judicialProducer.sendVoteToTopic(saleId))
                .map(tuple -> saleEntity);
    }

    private Mono<Tuple3<SaleDTO, PersonValidationDTO, PersonJudicialValidationDTO>> getPersonValidations(SaleDTO saleDTO) {
        String saleId = saleDTO.getId();

        return Mono.zip(
                personValidationService.getPersonValidationBySaleId(saleId),
                personJudicialValidationService.getJudicialValidationBySaleId(saleId)
        ).map(tuple -> Tuples.of(saleDTO, tuple.getT1(), tuple.getT2()));
    }
}
