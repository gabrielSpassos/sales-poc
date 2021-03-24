package com.gabrielspassos.poc.entity;

import com.gabrielspassos.poc.enumerator.PersonJudicialValidationStatusEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "personJudicialValidation")
public class PersonJudicialValidationEntity {

    @Id
    private String id;
    private String saleId;
    private PersonJudicialValidationStatusEnum status;

}
