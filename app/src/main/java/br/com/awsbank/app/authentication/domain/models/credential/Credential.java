package br.com.awsbank.app.authentication.domain.models.credential;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Credential {
    private CredentialType type;
    private String value;
}
