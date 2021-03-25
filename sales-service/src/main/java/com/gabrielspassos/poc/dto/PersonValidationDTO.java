package com.gabrielspassos.poc.dto;

import com.gabrielspassos.poc.enumerator.PersonValidationStatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonValidationDTO {

    private String id;
    private String saleId;
    private PersonValidationStatusEnum status;

}
