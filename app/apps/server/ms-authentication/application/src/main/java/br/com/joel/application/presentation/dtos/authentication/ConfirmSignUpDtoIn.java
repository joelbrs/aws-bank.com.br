package br.com.joel.application.presentation.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record ConfirmSignUpDtoIn(
        @NotBlank
        @CPF
        String taxId,

        @NotBlank
        String totpCode
) {}
