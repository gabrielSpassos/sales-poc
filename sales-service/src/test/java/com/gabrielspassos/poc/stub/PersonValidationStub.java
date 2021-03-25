package com.gabrielspassos.poc.stub;

import com.gabrielspassos.poc.client.http.response.PersonResponse;
import com.gabrielspassos.poc.dto.PersonValidationDTO;
import com.gabrielspassos.poc.entity.PersonValidationEntity;
import com.gabrielspassos.poc.enumerator.PersonValidationStatusEnum;

public class PersonValidationStub {

    public static PersonValidationEntity createEntity(String id, String saleId, PersonValidationStatusEnum status) {
        return PersonValidationEntity.builder()
                .id(id)
                .saleId(saleId)
                .status(status)
                .build();
    }

    public static PersonResponse createResponse(PersonValidationStatusEnum status) {
        PersonResponse personResponse = new PersonResponse();
        personResponse.setStatus(status);
        return personResponse;
    }

    public static PersonValidationDTO createDTO(String id, String saleId, PersonValidationStatusEnum status) {
        return PersonValidationDTO.builder()
                .id(id)
                .saleId(saleId)
                .status(status)
                .build();
    }
}
