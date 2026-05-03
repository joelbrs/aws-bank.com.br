package br.com.awsbank.app.authentication.adapters.out;

import br.com.awsbank.app.authentication.adapters.out.dto.SignUpKeycloakDtoIn;
import br.com.awsbank.app.authentication.adapters.out.rest.client.KeycloakFeignClient;
import br.com.awsbank.app.authentication.domain.models.User;
import br.com.awsbank.app.authentication.ports.out.IdentityProviderPort;
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

            var userId = keycloakFeignClient.getUsers(user.getEmail()).getFirst().id();

            keycloakFeignClient.sendVerifyEmail(userId);
            log.info("[INFO] Sent verification email to user {} in Keycloak", user.getEmail());
        } catch (Exception ex) {
            log.error("[ERROR] Failed to sign up user {} in Keycloak: {}", user.getEmail(), ex.getMessage(), ex);
        }
    }
}
