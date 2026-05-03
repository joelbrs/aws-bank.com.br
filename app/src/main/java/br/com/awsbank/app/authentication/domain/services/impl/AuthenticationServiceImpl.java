package br.com.awsbank.app.authentication.domain.services.impl;

import br.com.awsbank.app.authentication.domain.models.User;
import br.com.awsbank.app.authentication.domain.services.AuthenticationService;
import br.com.awsbank.app.authentication.ports.out.IdentityProviderPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final IdentityProviderPort identityProviderPort;

    @Override
    public void signUp(User user) {
        identityProviderPort.signUp(user);
    }
}
