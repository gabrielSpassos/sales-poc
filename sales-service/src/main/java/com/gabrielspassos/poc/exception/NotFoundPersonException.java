package com.gabrielspassos.poc.exception;

import org.springframework.http.HttpStatus;

public class NotFoundPersonException extends HttpException {

    public NotFoundPersonException() {
        super(HttpStatus.NOT_FOUND, "Não encontrado pessoa", "6");
    }
}
