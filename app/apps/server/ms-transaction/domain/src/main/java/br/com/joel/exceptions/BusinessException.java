package br.com.joel.exceptions;

import java.util.List;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    public BusinessException(String message, List<String> messageErrors) {
        super(message + " Errors: " + String.join(", ", messageErrors));
    }
}
