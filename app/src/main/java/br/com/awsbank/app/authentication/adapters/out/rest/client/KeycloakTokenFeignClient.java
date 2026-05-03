package br.com.awsbank.app.authentication.adapters.out.rest.client;

import br.com.awsbank.app.authentication.domain.models.Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "keycloak-token-client",
        url = "${rest.api.keycloak.base-url}"
)
public interface KeycloakTokenFeignClient {

    @PostMapping(value = "/realms/${rest.api.keycloak.realm}/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Token getToken(@RequestBody MultiValueMap<String, String> form);
}
