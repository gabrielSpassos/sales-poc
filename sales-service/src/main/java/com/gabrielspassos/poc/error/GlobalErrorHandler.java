package com.gabrielspassos.poc.error;

import com.gabrielspassos.poc.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {

    private static final String DEFAULT_MESSAGE = "Error interno";
    private static final String DEFAULT_CODE = "99999999";
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    @ExceptionHandler
    public ResponseEntity<SimpleError> handleException(Throwable throwable) {
        if(throwable instanceof HttpException) {
            HttpException e = (HttpException) throwable;
            log.error("Erro tratado", e);
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(buildSimpleError(e.getMessage(), e.getCode(), e));
        }
        return buildInternalError(throwable);
    }

    private ResponseEntity<SimpleError> buildInternalError(Throwable throwable) {
        log.error("Erro não esperado", throwable);
        return ResponseEntity
                .status(DEFAULT_STATUS)
                .body(buildSimpleError(DEFAULT_MESSAGE, DEFAULT_CODE, throwable));
    }

    private SimpleError buildSimpleError(String message, String code, Throwable throwable) {
        log.error("Message {}, Code {}", message, code, throwable);
        return new SimpleError(message, code);
    }
}
