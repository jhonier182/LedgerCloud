package com.ledgercloud.shared.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

// @RestControllerAdvice Hace que Spring aplique este handler a todos los controllers REST.
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Captura excepciones en los controllers y las convierte en respuestas HTTP con ErrorResponse
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex){
        HttpStatus status = mapCodeToStatus(ex.getCode());
        return ResponseEntity.status(status).body(
            new ErrorResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(
            new ErrorResponse("INTERNAL_SERVER_ERROR", "Error interno del servidor"));
    }

    private HttpStatus mapCodeToStatus(String code){
        return switch(code){
            case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "BAD_REQUEST" -> HttpStatus.BAD_REQUEST;
            case "UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
            case "FORBIDDEN" -> HttpStatus.FORBIDDEN;
            case "INTERNAL_SERVER_ERROR" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
