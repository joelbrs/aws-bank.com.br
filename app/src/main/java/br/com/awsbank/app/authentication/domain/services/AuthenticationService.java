package br.com.awsbank.app.authentication.domain.services;

import br.com.awsbank.app.authentication.domain.models.User;

public interface AuthenticationService {
    void signUp(User user);
    void resendEmailVerification(String email);
}
