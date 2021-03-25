package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.dto.PersonDTOBuilder;
import com.gabrielspassos.poc.builder.dto.PersonValidationDTOBuilder;
import com.gabrielspassos.poc.builder.entity.PersonValidationEntityBuilder;
import com.gabrielspassos.poc.client.http.PersonServiceClient;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.PersonValidationDTO;
import com.gabrielspassos.poc.entity.PersonValidationEntity;
import com.gabrielspassos.poc.repository.PersonValidationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class PersonValidationService {

    private final PersonServiceClient personServiceClient;
    private final PersonValidationRepository personValidationRepository;

    public Mono<PersonValidationEntity> createPersonValidation(SaleEvent saleEvent) {
        log.info("Criando validação de pessoa {}", saleEvent);
        PersonDTO personDTO = PersonDTOBuilder.build(saleEvent.getPerson());
        return personServiceClient.getPersonValidationStatus(personDTO)
                .map(personResponse -> PersonValidationEntityBuilder.build(saleEvent.getId(), personResponse))
                .flatMap(personValidationRepository::save);
    }

    public Mono<PersonValidationDTO> getPersonValidationBySaleId(String saleId) {
        log.info("Buscando validação de pessoa da venda {}", saleId);
        return personValidationRepository.findBySaleId(saleId)
                .switchIfEmpty(Mono.error(new RuntimeException())) //todo: rever
                .map(PersonValidationDTOBuilder::build)
                .doOnSuccess(dto -> log.info("Localizado validação de pessoa da venda {}: {}", saleId, dto));
    }

}
