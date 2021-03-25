package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.dto.PersonJudicialValidationDTOBuilder;
import com.gabrielspassos.poc.builder.entity.PersonJudicialValidationEntityBuilder;
import com.gabrielspassos.poc.client.http.JudicialServiceClient;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.dto.PersonJudicialValidationDTO;
import com.gabrielspassos.poc.entity.PersonJudicialValidationEntity;
import com.gabrielspassos.poc.repository.PersonJudicialValidationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class PersonJudicialValidationService {

    private final SaleService saleService;
    private final JudicialServiceClient judicialServiceClient;
    private final PersonJudicialValidationRepository personJudicialValidationRepository;

    public Mono<PersonJudicialValidationEntity> createPersonJudicialValidation(SaleEvent saleEvent) {
        log.info("Criando validação judicial {}", saleEvent);
        return saleService.getSaleById(saleEvent.getId())
                .flatMap(saleEntity -> judicialServiceClient.getJudicialValidationStatus(saleEntity.getPerson().getNationalIdentificationNumber()))
                .map(judicialResponse -> PersonJudicialValidationEntityBuilder.build(saleEvent.getId(), judicialResponse))
                .flatMap(personJudicialValidationRepository::save);
    }

    public Mono<PersonJudicialValidationDTO> getJudicialValidationBySaleId(String saleId) {
        log.info("Buscando validação judicial da venda {}", saleId);
        return personJudicialValidationRepository.findBySaleId(saleId)
                .switchIfEmpty(Mono.error(new RuntimeException())) //todo: rever
                .map(PersonJudicialValidationDTOBuilder::build)
                .doOnSuccess(dto -> log.info("Localizado validação judicial da venda {}: {}", saleId, dto));
    }
}