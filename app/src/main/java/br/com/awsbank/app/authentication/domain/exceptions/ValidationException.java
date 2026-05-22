package br.com.awsbank.app.authentication.domain.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {

    @Getter
    private List<ValidationExceptionDomainModel> errors = new ArrayList<>();

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(List<ValidationExceptionDomainModel> errors) {
        super("Validation failed with multiple errors.");
        this.errors = errors;
    }

    public record ValidationExceptionDomainModel(
            String message,
            String field
    ) {}
}
