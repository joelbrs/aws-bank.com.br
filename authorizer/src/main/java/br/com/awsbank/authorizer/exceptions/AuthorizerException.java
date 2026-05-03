package br.com.awsbank.authorizer.exceptions;

public class AuthorizerException extends RuntimeException{
    public AuthorizerException(String message) {
        super(message);
    }

    public AuthorizerException(String message, Throwable cause) {
        super(message, cause);
    }
}
