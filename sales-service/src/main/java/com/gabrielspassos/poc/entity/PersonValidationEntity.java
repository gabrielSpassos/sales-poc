package com.gabrielspassos.poc.entity;

import com.gabrielspassos.poc.enumerator.PersonValidationStatusEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "personValidation")
public class PersonValidationEntity {

    @Id
    private String id;
    private String saleId;
    private PersonValidationStatusEnum status;

}
