package com.gabrielspassos.poc.client.http.response;

import com.gabrielspassos.poc.enumerator.PersonJudicialValidationStatusEnum;
import lombok.Data;

@Data
public class JudicialResponse {

    private PersonJudicialValidationStatusEnum status;
    private String judicialValidatorEntity;

}
