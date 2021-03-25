package com.gabrielspassos.poc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public abstract class HttpException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;
    private String code;

}
