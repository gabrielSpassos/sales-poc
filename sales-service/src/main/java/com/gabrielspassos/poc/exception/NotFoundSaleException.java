package com.gabrielspassos.poc.exception;

import org.springframework.http.HttpStatus;

public class NotFoundSaleException extends HttpException {

    public NotFoundSaleException() {
        super(HttpStatus.NOT_FOUND, "Não encontrado venda", "5");
    }
}
