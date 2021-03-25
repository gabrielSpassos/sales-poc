package com.gabrielspassos.poc.builder.dto;

import com.gabrielspassos.poc.dto.PersonJudicialValidationDTO;
import com.gabrielspassos.poc.entity.PersonJudicialValidationEntity;

public class PersonJudicialValidationDTOBuilder {

    public static PersonJudicialValidationDTO build(PersonJudicialValidationEntity entity) {
        return PersonJudicialValidationDTO.builder()
                .id(entity.getId())
                .saleId(entity.getSaleId())
                .status(entity.getStatus())
                .build();
    }
}
