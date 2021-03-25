package com.gabrielspassos.poc.dto;

import com.gabrielspassos.poc.enumerator.PersonJudicialValidationStatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonJudicialValidationDTO {

    private String id;
    private String saleId;
    private PersonJudicialValidationStatusEnum status;

}
