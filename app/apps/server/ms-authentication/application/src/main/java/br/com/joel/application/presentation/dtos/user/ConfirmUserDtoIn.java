package br.com.joel.application.presentation.dtos.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record ConfirmUserDtoIn(
        @NotBlank
        @CPF
        String taxId,

        @NotBlank
        String totpCode
) {}
