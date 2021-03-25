package com.gabrielspassos.poc.exception;

import org.springframework.http.HttpStatus;

public class ScoreSaleValidationException extends HttpException {

    public ScoreSaleValidationException() {
        super(HttpStatus.BAD_REQUEST, "Score da pessoa da venda invalido", "4");
    }
}
