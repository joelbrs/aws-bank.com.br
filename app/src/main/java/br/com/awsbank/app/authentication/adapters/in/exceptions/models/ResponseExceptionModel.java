package br.com.awsbank.app.authentication.adapters.in.exceptions.models;

import java.time.Instant;

public record ResponseExceptionModel (
        Instant timestamp,
        Integer status,
        String error,
        String path
) {}
