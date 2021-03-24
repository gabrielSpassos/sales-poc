package com.gabrielspassos.poc.controller.v1.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonRequest {

    private String nationalIdentificationNumber;
    private LocalDate birthdate;
    private String firstName;
    private String lastName;
    private String email;

}
