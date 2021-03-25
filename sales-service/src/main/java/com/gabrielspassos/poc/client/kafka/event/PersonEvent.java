package com.gabrielspassos.poc.client.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonEvent {

    private String nationalIdentificationNumber;
    private String birthdate;
    private String firstName;
    private String lastName;
    private String email;

}
