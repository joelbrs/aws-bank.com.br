package br.com.awsbank.app.authentication.adapters.in.exceptions.handlers;

import br.com.awsbank.app.authentication.adapters.in.exceptions.models.ResponseExceptionModel;
import br.com.awsbank.app.authentication.adapters.in.exceptions.models.ValidationExceptionModel;
import br.com.awsbank.app.authentication.domain.exceptions.ExternalServiceException;
import br.com.awsbank.app.authentication.domain.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseExceptionModel externalServiceException(
            HttpServletRequest request,
            ExternalServiceException ex
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();
        return new ResponseExceptionModel(Instant.now(), status.value(), message, request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ValidationExceptionModel validationException(
            HttpServletRequest request,
            ConstraintViolationException ex
    ) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        String message = "Some fields are not valid.";
        ValidationExceptionModel exception = new ValidationExceptionModel(new ResponseExceptionModel(Instant.now(), status.value(), message, request.getRequestURI()), new ArrayList<>());

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            exception.add(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return exception;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ValidationExceptionModel methodArgumentNotValid(
            HttpServletRequest request,
            MethodArgumentNotValidException ex
    ) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        String message = "Some fields are not valid.";
        ValidationExceptionModel exception = new ValidationExceptionModel(new ResponseExceptionModel(Instant.now(), status.value(), message, request.getRequestURI()), new ArrayList<>());

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            exception.add(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return exception;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ValidationExceptionModel bindException(
            HttpServletRequest request,
            BindException ex
    ) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        String message = "Some fields are not valid.";
        ValidationExceptionModel exception = new ValidationExceptionModel(new ResponseExceptionModel(Instant.now(), status.value(), message, request.getRequestURI()), new ArrayList<>());

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            exception.add(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return exception;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ValidationExceptionModel ownValidationException(
            HttpServletRequest request,
            ValidationException ex
    ) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        String message = "Some fields are not valid.";
        ValidationExceptionModel exception = new ValidationExceptionModel(new ResponseExceptionModel(Instant.now(), status.value(), message, request.getRequestURI()), new ArrayList<>());

        ex.getErrors().forEach(error -> {
            exception.add(error.field(), error.message());
        });

        return exception;
    }
}
