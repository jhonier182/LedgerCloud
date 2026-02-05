package com.ledgercloud.shared.exceptions;

import java.time.Instant;

public record ErrorResponse(String code, String message, Instant timestamp) {


    //DTO que define el formato JSON de las respuestas de error.
    public ErrorResponse(String code, String message){
        this(code, message, Instant.now());
    }
}
