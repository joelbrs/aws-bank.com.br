package br.com.awsbank.app.authentication.ports.in.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendEmailDtoIn(
        @NotBlank
        @Email
        String email
) {}
