package com.gabrielspassos.poc.builder.entity;

import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.entity.PersonEntity;

public class PersonEntityBuilder {

    public static PersonEntity build(PersonDTO personDTO) {
        return PersonEntity.builder()
                .nationalIdentificationNumber(personDTO.getNationalIdentificationNumber())
                .firstName(personDTO.getFirstName())
                .lastName(personDTO.getLastName())
                .birthdate(personDTO.getBirthdate())
                .email(personDTO.getEmail())
                .build();
    }
}
