package br.com.awsbank.app.authentication.domain.exceptions;

import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {

    private final Integer statusCode;

    public ExternalServiceException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ExternalServiceException(String message, Throwable cause, Integer statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
