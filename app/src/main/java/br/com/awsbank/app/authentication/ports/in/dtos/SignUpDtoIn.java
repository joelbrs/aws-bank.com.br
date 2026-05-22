package br.com.awsbank.app.authentication.ports.in.dtos;

import br.com.awsbank.app.authentication.domain.models.User;
import br.com.awsbank.app.authentication.domain.models.credential.Credential;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

public record SignUpDtoIn(
        @CPF
        String cpf,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        @NotBlank
        String email,

        @NotNull
        @Size(min = 2, max = 2)
        List<Credential> credentials
) {
        public User toDomain() {
                return User.builder()
                        .cpf(cpf)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .credentials(credentials)
                        .isVerified(false)
                        .build();
        }
}
