package com.gabrielspassos.poc.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleError {

    private String errorMessage;
    private String errorCode;

}
