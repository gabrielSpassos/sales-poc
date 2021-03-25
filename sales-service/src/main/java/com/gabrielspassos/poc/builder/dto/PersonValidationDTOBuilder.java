package com.gabrielspassos.poc.builder.dto;

import com.gabrielspassos.poc.dto.PersonValidationDTO;
import com.gabrielspassos.poc.entity.PersonValidationEntity;

public class PersonValidationDTOBuilder {

    public static PersonValidationDTO build(PersonValidationEntity entity) {
        return PersonValidationDTO.builder()
                .id(entity.getId())
                .saleId(entity.getSaleId())
                .status(entity.getStatus())
                .build();
    }
}
