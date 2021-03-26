package com.gabrielspassos.poc.stub;

import com.gabrielspassos.poc.client.http.response.JudicialResponse;
import com.gabrielspassos.poc.dto.PersonJudicialValidationDTO;
import com.gabrielspassos.poc.entity.PersonJudicialValidationEntity;
import com.gabrielspassos.poc.enumerator.PersonJudicialValidationStatusEnum;

public class PersonJudicialStub {

    public static PersonJudicialValidationEntity create(String id, String saleId, PersonJudicialValidationStatusEnum status) {
        return PersonJudicialValidationEntity.builder()
                .id(id)
                .saleId(saleId)
                .status(status)
                .build();
    }

    public static JudicialResponse createResponse(String entity, PersonJudicialValidationStatusEnum status) {
        JudicialResponse judicialResponse = new JudicialResponse();
        judicialResponse.setJudicialValidatorEntity(entity);
        judicialResponse.setStatus(status);
        return judicialResponse;
    }

    public static String createResponseBody() {
        return "{\n" +
                "    \"status\": \"REPROVED\",\n" +
                "    \"judicialValidatorEntity\": \"STF-BR\"\n" +
                "}";
    }

    public static PersonJudicialValidationDTO createDTO(String id, String saleId, PersonJudicialValidationStatusEnum status) {
        return PersonJudicialValidationDTO.builder()
                .id(id)
                .saleId(saleId)
                .status(status)
                .build();
    }
}
