package br.com.awsbank.app.authentication.ports.out;

import br.com.awsbank.app.authentication.domain.exceptions.ExternalServiceException;
import br.com.awsbank.app.authentication.domain.models.User;

public interface IdentityProviderPort {
    void signUp(User user) throws ExternalServiceException;
    void sendEmailVerification(String email) throws ExternalServiceException;
}
