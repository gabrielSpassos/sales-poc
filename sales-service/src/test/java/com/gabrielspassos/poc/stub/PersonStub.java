package com.gabrielspassos.poc.stub;

import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.dto.PersonDTO;

import java.time.LocalDate;

public class PersonStub {

    public static PersonDTO createDTO(String nationalIdentificationNumber, String firstName, String lastName,
                                      String email, LocalDate dateTime) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setNationalIdentificationNumber(nationalIdentificationNumber);
        personDTO.setFirstName(firstName);
        personDTO.setLastName(lastName);
        personDTO.setEmail(email);
        personDTO.setBirthdate(dateTime);
        return personDTO;
    }

    public static PersonEvent createEvent(String nationalIdentificationNumber, String firstName, String lastName,
                                          String email, String dateTime) {
        PersonEvent personEvent = new PersonEvent();
        personEvent.setNationalIdentificationNumber(nationalIdentificationNumber);
        personEvent.setFirstName(firstName);
        personEvent.setLastName(lastName);
        personEvent.setEmail(email);
        personEvent.setBirthdate(dateTime);
        return personEvent;
    }
}
