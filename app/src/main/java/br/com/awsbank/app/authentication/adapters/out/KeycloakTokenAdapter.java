package br.com.awsbank.app.authentication.adapters.out;

import br.com.awsbank.app.authentication.adapters.out.rest.client.KeycloakTokenFeignClient;
import br.com.awsbank.app.authentication.domain.models.Token;
import br.com.awsbank.app.authentication.ports.out.TokenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@RequiredArgsConstructor
public class KeycloakTokenAdapter implements TokenPort {

    @Value("${rest.api.keycloak.client-id}")
    private String clientId;

    @Value("${rest.api.keycloak.client-secret}")
    private String clientSecret;

    private final KeycloakTokenFeignClient keycloakTokenFeignClient;

    @Override
    public Token getToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        try {
            return keycloakTokenFeignClient.getToken(form);
        } catch (Exception e) {
            log.error("[ERROR] Failed to get token from Keycloak: {}", e.getMessage(), e);
        }

        return null;
    }
}
