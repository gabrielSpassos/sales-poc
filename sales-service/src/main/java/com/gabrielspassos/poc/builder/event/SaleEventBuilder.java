package com.gabrielspassos.poc.builder.event;

import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.dto.SaleDTO;

public class SaleEventBuilder {

    public static SaleEvent build(SaleDTO saleDTO) {
        PersonEvent person = PersonEventBuilder.build(saleDTO.getPerson());

        return SaleEvent.builder()
                .id(saleDTO.getId())
                .person(person)
                .build();
    }
}
