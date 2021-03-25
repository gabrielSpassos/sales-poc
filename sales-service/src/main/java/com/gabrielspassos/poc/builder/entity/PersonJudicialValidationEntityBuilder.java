package com.gabrielspassos.poc.builder.entity;

import com.gabrielspassos.poc.client.http.response.JudicialResponse;
import com.gabrielspassos.poc.entity.PersonJudicialValidationEntity;

public class PersonJudicialValidationEntityBuilder {

    public static PersonJudicialValidationEntity build(String saleId, JudicialResponse judicialResponse) {
        return PersonJudicialValidationEntity.builder()
                .id(null)
                .saleId(saleId)
                .status(judicialResponse.getStatus())
                .build();
    }
}
