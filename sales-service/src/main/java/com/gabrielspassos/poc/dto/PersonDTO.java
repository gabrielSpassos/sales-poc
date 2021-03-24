package com.gabrielspassos.poc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private String nationalIdentificationNumber;
    private LocalDate birthdate;
    private String firstName;
    private String lastName;
    private String email;

}

