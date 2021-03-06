package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.dto.PersonJudicialValidationDTOBuilder;
import com.gabrielspassos.poc.builder.entity.PersonJudicialValidationEntityBuilder;
import com.gabrielspassos.poc.client.http.JudicialServiceClient;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.dto.PersonJudicialValidationDTO;
import com.gabrielspassos.poc.exception.NotFoundPersonJudicialValidationException;
import com.gabrielspassos.poc.repository.PersonJudicialValidationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class PersonJudicialValidationService {

    private final JudicialServiceClient judicialServiceClient;
    private final PersonJudicialValidationRepository personJudicialValidationRepository;

    public Mono<PersonJudicialValidationDTO> createPersonJudicialValidation(SaleEvent saleEvent) {
        log.info("Criando validação judicial {}", saleEvent);
        return judicialServiceClient.getJudicialValidationStatus(saleEvent.getPerson().getNationalIdentificationNumber())
                .map(judicialResponse -> PersonJudicialValidationEntityBuilder.build(saleEvent.getId(), judicialResponse))
                .flatMap(personJudicialValidationRepository::save)
                .map(PersonJudicialValidationDTOBuilder::build);
    }

    public Mono<PersonJudicialValidationDTO> getJudicialValidationBySaleId(String saleId) {
        log.info("Buscando validação judicial da venda {}", saleId);
        return personJudicialValidationRepository.findBySaleId(saleId)
                .switchIfEmpty(Mono.error(new NotFoundPersonJudicialValidationException()))
                .map(PersonJudicialValidationDTOBuilder::build)
                .doOnSuccess(dto -> log.info("Localizado validação judicial da venda {}: {}", saleId, dto));
    }
}
