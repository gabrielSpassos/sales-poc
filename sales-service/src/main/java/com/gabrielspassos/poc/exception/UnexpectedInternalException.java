package com.gabrielspassos.poc.exception;

import org.springframework.http.HttpStatus;

public class UnexpectedInternalException extends HttpException{

    public UnexpectedInternalException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", "6");
    }
}
