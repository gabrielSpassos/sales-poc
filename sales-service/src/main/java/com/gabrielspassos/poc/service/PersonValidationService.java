package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.dto.PersonDTOBuilder;
import com.gabrielspassos.poc.builder.entity.PersonValidationEntityBuilder;
import com.gabrielspassos.poc.client.http.PersonServiceClient;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
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

    private final SaleService saleService;
    private final PersonServiceClient personServiceClient;
    private final PersonValidationRepository personValidationRepository;

    public Mono<PersonValidationEntity> createPersonValidation(SaleEvent saleEvent) {
        log.info("Criando validação de pessoa {}", saleEvent);
        return saleService.getSaleById(saleEvent.getId())
                .map(saleEntity -> PersonDTOBuilder.build(saleEntity.getPerson()))
                .flatMap(personServiceClient::getPersonValidationStatus)
                .map(personResponse -> PersonValidationEntityBuilder.build(saleEvent.getId(), personResponse))
                .flatMap(personValidationRepository::save);
    }

}
