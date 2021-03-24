package com.gabrielspassos.poc.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "personScore")
public class PersonScoreEntity {

    @Id
    private String id;
    private String saleId;
    private Integer score;

}
