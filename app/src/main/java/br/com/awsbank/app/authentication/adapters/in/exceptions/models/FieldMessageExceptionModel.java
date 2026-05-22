package br.com.awsbank.app.authentication.adapters.in.exceptions.models;

public record FieldMessageExceptionModel(
        String field,
        String message
) {}
