package com.gabrielspassos.poc.builder.dto;

import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.entity.PersonEntity;

import java.time.LocalDate;

public class PersonDTOBuilder {

    public static PersonDTO build(PersonEntity personEntity) {
        return PersonDTO.builder()
                .nationalIdentificationNumber(personEntity.getNationalIdentificationNumber())
                .firstName(personEntity.getFirstName())
                .lastName(personEntity.getLastName())
                .birthdate(personEntity.getBirthdate())
                .email(personEntity.getEmail())
                .build();
    }

    public static PersonDTO build(PersonEvent personEvent) {
        return PersonDTO.builder()
                .nationalIdentificationNumber(personEvent.getNationalIdentificationNumber())
                .firstName(personEvent.getFirstName())
                .lastName(personEvent.getLastName())
                .birthdate(LocalDate.parse(personEvent.getBirthdate()))
                .email(personEvent.getEmail())
                .build();
    }
}
