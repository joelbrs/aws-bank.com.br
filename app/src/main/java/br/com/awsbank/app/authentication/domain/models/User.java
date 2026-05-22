package br.com.awsbank.app.authentication.domain.models;

import br.com.awsbank.app.authentication.domain.exceptions.ValidationException;
import br.com.awsbank.app.authentication.domain.models.credential.Credential;
import br.com.awsbank.app.authentication.domain.models.credential.CredentialType;
import br.com.awsbank.app.commons.utils.validators.CPFFieldValidator;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class User {
    private String cpf;
    private String firstName;
    private String email;
    private String lastName;
    private List<Credential> credentials;
    private Boolean isVerified;

    public User(
            String cpf,
            String firstName,
            String email,
            String lastName,
            List<Credential> credentials,
            Boolean isVerified
    ) {
        this.validate(cpf, credentials);

        this.cpf = cpf.replaceAll("\\D", "");
        this.firstName = firstName;
        this.email = email;
        this.lastName = lastName;
        this.credentials = credentials;
        this.isVerified = isVerified;
    }

    private void validate(String cpf, List<Credential> credentials) {
        if (!CPFFieldValidator.isValid(cpf)) {
            throw new ValidationException("Invalid CPF.");
        }

        int credentialCount = CredentialType.values().length;

        if (credentials == null || credentials.isEmpty() || credentials.size() != credentialCount) {
            throw new ValidationException("User must have 2 credentials.");
        }

        Set<CredentialType> credentialTypes = credentials.stream()
                .map(Credential::getType)
                .collect(Collectors.toSet());

        boolean hasAllCredentialTypes =
                Arrays.stream(CredentialType.values())
                        .allMatch(credentialTypes::contains);

        if (credentialTypes.contains(null) || !hasAllCredentialTypes) {
            throw new ValidationException("User must have exactly 2 credentials: PASSWORD and TRANSACTION_PASSWORD.");
        }

        List<ValidationException.ValidationExceptionDomainModel> invalidCredentialsValidationExceptions = credentials.stream()
                .map(credential -> credential.getType().validate(credential.getValue(), cpf))
                .filter(Objects::nonNull)
                .toList();

        if (!invalidCredentialsValidationExceptions.isEmpty()) {
            throw new ValidationException(invalidCredentialsValidationExceptions);
        }
    }
}
