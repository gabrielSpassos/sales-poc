package com.gabrielspassos.poc.stub;

import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.enumerator.SaleStatusEnum;

import java.time.LocalDateTime;

public class SaleStub {

    public static SaleDTO createDTO(String id, SaleStatusEnum status, LocalDateTime dateTime, PersonDTO personDTO) {
        return SaleDTO.builder()
                .id(id)
                .status(status)
                .registerDateTime(dateTime)
                .person(personDTO)
                .build();
    }

    public static SaleEvent createEvent(String id, PersonEvent personEvent) {
        SaleEvent saleEvent = new SaleEvent();
        saleEvent.setId(id);
        saleEvent.setPerson(personEvent);
        return saleEvent;
    }
}
