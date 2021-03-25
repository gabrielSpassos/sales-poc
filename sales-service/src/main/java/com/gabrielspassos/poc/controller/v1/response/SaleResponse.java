package com.gabrielspassos.poc.controller.v1.response;

import com.gabrielspassos.poc.enumerator.SaleStatusEnum;
import lombok.Data;

@Data
public class SaleResponse {

    private String id;
    private SaleStatusEnum status;
    private String registerDateTime;
    private PersonResponse person;

}
