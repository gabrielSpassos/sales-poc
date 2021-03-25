package com.gabrielspassos.poc.builder.dto;

import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.entity.SaleEntity;

public class SaleDTOBuilder {

    public static SaleDTO build(SaleEntity entity) {
        PersonDTO personDTO = PersonDTOBuilder.build(entity.getPerson());

        return SaleDTO.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .registerDateTime(entity.getRegisterDateTime())
                .person(personDTO)
                .build();
    }
}
