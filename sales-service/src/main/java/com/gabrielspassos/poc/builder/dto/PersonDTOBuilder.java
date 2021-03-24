package com.gabrielspassos.poc.builder.dto;

import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.entity.PersonEntity;

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
}
