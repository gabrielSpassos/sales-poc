package com.gabrielspassos.poc.exception;

import org.springframework.http.HttpStatus;

public class NotFoundPersonValidationException extends HttpException {

    public NotFoundPersonValidationException() {
        super(HttpStatus.NOT_FOUND, "Não encontrado validação da pessoa", "1");
    }
}
