package com.gabrielspassos.poc.builder.event;

import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.dto.PersonDTO;

public class PersonEventBuilder {

    public static PersonEvent build(PersonDTO personDTO) {
        return PersonEvent.builder()
                .nationalIdentificationNumber(personDTO.getNationalIdentificationNumber())
                .firstName(personDTO.getFirstName())
                .lastName(personDTO.getLastName())
                .birthdate(personDTO.getBirthdate().toString())
                .email(personDTO.getEmail())
                .build();
    }
}
