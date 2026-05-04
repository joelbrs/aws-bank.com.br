package br.com.awsbank.app.authentication.adapters.out;

import br.com.awsbank.app.authentication.adapters.out.dto.SignUpKeycloakDtoIn;
import br.com.awsbank.app.authentication.adapters.out.rest.client.KeycloakFeignClient;
import br.com.awsbank.app.authentication.domain.exceptions.ExternalServiceException;
import br.com.awsbank.app.authentication.domain.models.User;
import br.com.awsbank.app.authentication.ports.out.IdentityProviderPort;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class KeycloakIdentityProviderAdapter implements IdentityProviderPort {

    private final KeycloakFeignClient keycloakFeignClient;

    @Override
    public void signUp(User user) {
        var signUpRequest = SignUpKeycloakDtoIn.fromDomain(user);

        log.info("[INFO] Signing up user {} in Keycloak", user.getEmail());

        try {
            keycloakFeignClient.signUp(signUpRequest);
            log.info("[INFO] Signed up user successfully {} in Keycloak", user.getEmail());

            this.sendEmailVerification(user.getEmail());
        } catch (FeignException ex) {
            log.error("[ERROR] Failed to sign up user {} in Keycloak: {}", user.getEmail(), ex.getMessage(), ex);
            throw new ExternalServiceException("Failed to sign up user in Keycloak: " + ex.getMessage(), ex, ex.status());
        }
    }

    @Override
    public void sendEmailVerification(String email) {
        try {
            log.info("[INFO] Fetching user's id by email : {}", email);
            var userId = keycloakFeignClient.getUsers(email).getFirst().id();

            keycloakFeignClient.sendVerifyEmail(userId);
            log.info("[INFO] Sent verification email to user {} in Keycloak", email);
        } catch (FeignException ex) {
            log.error("[ERROR] Failed to send verification email to user {} in Keycloak: {}", email, ex.getMessage(), ex);
            throw new ExternalServiceException("Failed to send verification email in Keycloak: " + ex.getMessage(), ex, ex.status());
        }
    }
}
