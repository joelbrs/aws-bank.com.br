package br.com.joel.application.presentation.dtos.authentication;

import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.UserPassword;
import br.com.joel.exceptions.BusinessException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record SignUpDtoIn(

    @NotBlank
    String name,
    String lastName,

    @NotBlank
    @CPF
    String taxId,

    @NotBlank
    @Email
    String email,

    @NotNull
    UserPasswordDtoIn passwords
) {
    public User toDomain() {
        passwords().validate();

        UserPassword userPassword = UserPassword.builder()
                .actionsPassword(passwords().actionsPassword())
                .loginPassword(passwords().loginPassword())
                .build();

        return User.builder()
                .name(name)
                .lastName(lastName)
                .taxId(taxId)
                .email(email)
                .password(userPassword)
                .build();
    }
}

record UserPasswordDtoIn(
    @NotBlank
    String loginPassword,

    @NotBlank
    String actionsPassword,
    String confirmLoginPassword,
    String confirmActionsPassword
) {
    public void validate() {
        if (!loginPassword.equals(confirmLoginPassword)) {
            throw new BusinessException("Login password do not match");
        }
        if (!actionsPassword.equals(confirmActionsPassword)) {
            throw new BusinessException("Actions password do not match");
        }
    }
}
