package com.gabrielspassos.poc.exception;

import org.springframework.http.HttpStatus;

public class NotFoundPersonJudicialValidationException extends HttpException {

    public NotFoundPersonJudicialValidationException() {
        super(HttpStatus.NOT_FOUND, "Não encontrado validação judicial da pessoa", "2");
    }
}
