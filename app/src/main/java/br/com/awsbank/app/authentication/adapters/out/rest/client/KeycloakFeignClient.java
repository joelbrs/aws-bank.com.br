package br.com.awsbank.app.authentication.adapters.out.rest.client;

import br.com.awsbank.app.authentication.adapters.out.dto.ListUserKeycloakDtoOut;
import br.com.awsbank.app.authentication.adapters.out.dto.SignUpKeycloakDtoIn;
import br.com.awsbank.app.authentication.adapters.out.rest.client.config.KeycloakFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "keycloak-client",
        url = "${rest.api.keycloak.base-url}",
        configuration = KeycloakFeignClientConfig.class
)
public interface KeycloakFeignClient {

    @PostMapping("/admin/realms/${rest.api.keycloak.realm}/users")
    void signUp(@RequestBody SignUpKeycloakDtoIn signUpKeycloakDtoIn);

    @GetMapping( "/admin/realms/${rest.api.keycloak.realm}/users")
    List<ListUserKeycloakDtoOut> getUsers(@RequestParam("email") String email);

    @PutMapping("/admin/realms/${rest.api.keycloak.realm}/users/{user-id}/send-verify-email")
    void sendVerifyEmail(@PathVariable("user-id") String userId);
}
