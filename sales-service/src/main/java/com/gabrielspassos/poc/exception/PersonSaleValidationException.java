package com.gabrielspassos.poc.exception;

import org.springframework.http.HttpStatus;

public class PersonSaleValidationException extends HttpException {

    public PersonSaleValidationException() {
        super(HttpStatus.BAD_REQUEST, "Pessoa da venda com validação reprovada", "3");
    }
}
