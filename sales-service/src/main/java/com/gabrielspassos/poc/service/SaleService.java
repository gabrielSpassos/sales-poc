package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.entity.SaleEntityBuilder;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.entity.SaleEntity;
import com.gabrielspassos.poc.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    public Mono<SaleEntity> createNewSale(PersonDTO personDTO) {
        SaleEntity saleEntity = SaleEntityBuilder.build(personDTO);

        return saleRepository.save(saleEntity);
    }
}
