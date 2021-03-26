package com.gabrielspassos.poc.controller.v1.response;

import lombok.Data;

@Data
public class PersonResponse {

    private String nationalIdentificationNumber;
    private String birthdate;
    private String firstName;
    private String lastName;
    private String email;

}
