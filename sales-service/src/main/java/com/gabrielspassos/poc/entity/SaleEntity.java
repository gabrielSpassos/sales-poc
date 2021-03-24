package com.gabrielspassos.poc.entity;

import com.gabrielspassos.poc.enumerator.SaleStatusEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "sale")
public class SaleEntity {

    @Id
    private String id;
    private SaleStatusEnum status;
    private LocalDateTime registerDateTime;
    private PersonEntity person;

}
