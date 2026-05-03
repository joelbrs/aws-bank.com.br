package br.com.awsbank.app.authentication.adapters.out.dto;

import br.com.awsbank.app.authentication.domain.models.User;
import br.com.awsbank.app.authentication.domain.models.credential.CredentialType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record SignUpKeycloakDtoIn(
   String username,
   String email,
   String firstName,
   String lastName,
   Boolean enabled,
   Boolean emailVerified,
   Map<String, List<String>> attributes,
   List<KeycloakCredentialDtoIn> credentials,
   List<String> requiredActions
) {
    public static SignUpKeycloakDtoIn fromDomain(User user) {
        List<KeycloakCredentialDtoIn> credentials = user.getCredentials().stream().map(
                credential -> new KeycloakCredentialDtoIn(
                        CredentialType.PASSWORD.getType(),
                        credential.getValue(),
                        false,
                        credential.getType().getType()
                )
        ).toList();

        return new SignUpKeycloakDtoIn(
                user.getCpf(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                true,
                false,
                Map.of("cpf", Collections.singletonList(user.getCpf())),
                credentials,
                Collections.singletonList("VERIFY_EMAIL")
        );
    }
}

record KeycloakCredentialDtoIn(
    String type,
    String value,
    Boolean temporary,
    String userLabel
) {}