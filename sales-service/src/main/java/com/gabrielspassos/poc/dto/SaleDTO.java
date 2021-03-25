package com.gabrielspassos.poc.dto;

import com.gabrielspassos.poc.enumerator.SaleStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SaleDTO {

    private String id;
    private SaleStatusEnum status;
    private LocalDateTime registerDateTime;
    private PersonDTO person;

}
