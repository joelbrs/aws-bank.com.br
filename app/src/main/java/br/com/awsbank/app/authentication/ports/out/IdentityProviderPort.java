package br.com.awsbank.app.authentication.ports.out;

import br.com.awsbank.app.authentication.domain.models.User;

public interface IdentityProviderPort {
    void signUp(User user);
}
