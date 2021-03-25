package com.gabrielspassos.poc.builder.entity;

import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.entity.PersonEntity;
import com.gabrielspassos.poc.entity.SaleEntity;
import com.gabrielspassos.poc.enumerator.SaleStatusEnum;

import java.time.LocalDateTime;

public class SaleEntityBuilder {

    public static SaleEntity build(PersonDTO personDTO) {
        PersonEntity personEntity = PersonEntityBuilder.build(personDTO);

        return SaleEntity.builder()
                .id(null)
                .registerDateTime(LocalDateTime.now())
                .status(SaleStatusEnum.LEAD)
                .person(personEntity)
                .build();
    }

    public static SaleEntity build(SaleDTO saleDTO, SaleStatusEnum status) {
        PersonEntity personEntity = PersonEntityBuilder.build(saleDTO.getPerson());

        return SaleEntity.builder()
                .id(saleDTO.getId())
                .registerDateTime(saleDTO.getRegisterDateTime())
                .status(status)
                .person(personEntity)
                .build();
    }
}
