package br.com.joel.application.presentation.exceptions.handler;

import br.com.joel.exceptions.BusinessException;
import br.com.joel.exceptions.ExternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ExternalServiceException.class)
    public String handleExternalException(ExternalServiceException ex) {
        return ex.getMessage();
    }
}
