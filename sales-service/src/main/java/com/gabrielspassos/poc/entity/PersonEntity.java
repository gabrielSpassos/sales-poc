package com.gabrielspassos.poc.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonEntity {

    private String nationalIdentificationNumber;
    private LocalDate birthdate;
    private String firstName;
    private String lastName;
    private String email;

}
