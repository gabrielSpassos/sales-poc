package com.gabrielspassos.poc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonScoreDTO {

    private String id;
    private String saleId;
    private Integer score;

}
