package com.gabrielspassos.poc.builder.entity;

import com.gabrielspassos.poc.client.http.response.PersonResponse;
import com.gabrielspassos.poc.entity.PersonValidationEntity;

public class PersonValidationEntityBuilder {

    public static PersonValidationEntity build(String saleId, PersonResponse personResponse) {
        return PersonValidationEntity.builder()
                .id(null)
                .saleId(saleId)
                .status(personResponse.getStatus())
                .build();
    }
}
