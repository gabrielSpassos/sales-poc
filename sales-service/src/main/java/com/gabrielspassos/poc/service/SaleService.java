package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.entity.SaleEntityBuilder;
import com.gabrielspassos.poc.client.kafka.JudicialProducer;
import com.gabrielspassos.poc.client.kafka.PersonProducer;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.entity.SaleEntity;
import com.gabrielspassos.poc.repository.SaleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final PersonProducer personProducer;
    private final JudicialProducer judicialProducer;

    public Mono<SaleEntity> createNewSale(PersonDTO personDTO) {
        SaleEntity saleEntity = SaleEntityBuilder.build(personDTO);

        return saleRepository.save(saleEntity)
                .flatMap(this::sendSaleEvent);
    }

    public Mono<SaleEntity> getSaleById(String id) {
        return saleRepository.findById(id)
                .doOnSuccess(saleEntity -> log.info("Encontrado venda {}", saleEntity));
    }

    private Mono<SaleEntity> sendSaleEvent(SaleEntity saleEntity) {
        String saleId = saleEntity.getId();

        return Mono.zip(personProducer.sendVoteToTopic(saleId), judicialProducer.sendVoteToTopic(saleId))
                .map(tuple -> saleEntity);
    }
}
