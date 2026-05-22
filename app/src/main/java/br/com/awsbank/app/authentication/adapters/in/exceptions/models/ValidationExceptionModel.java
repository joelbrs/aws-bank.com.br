package br.com.awsbank.app.authentication.adapters.in.exceptions.models;

import java.util.List;

public record ValidationExceptionModel(
        ResponseExceptionModel response,
        List<FieldMessageExceptionModel> messages
) {
    public void add(String field, String message) {
        messages.add(new FieldMessageExceptionModel(field, message));
    }
}
