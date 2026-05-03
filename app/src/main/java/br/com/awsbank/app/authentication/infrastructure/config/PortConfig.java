package br.com.awsbank.app.authentication.infrastructure.config;

import br.com.awsbank.app.authentication.adapters.out.KeycloakIdentityProviderAdapter;
import br.com.awsbank.app.authentication.adapters.out.KeycloakTokenAdapter;
import br.com.awsbank.app.authentication.adapters.out.rest.client.KeycloakFeignClient;
import br.com.awsbank.app.authentication.adapters.out.rest.client.KeycloakTokenFeignClient;
import br.com.awsbank.app.authentication.ports.out.IdentityProviderPort;
import br.com.awsbank.app.authentication.ports.out.TokenPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortConfig {

    @Bean
    IdentityProviderPort identityProviderPort(KeycloakFeignClient keycloakFeignClient) {
        return new KeycloakIdentityProviderAdapter(keycloakFeignClient);
    }

    @Bean
    TokenPort tokenPort(KeycloakTokenFeignClient keycloakTokenFeignClient) {
        return new KeycloakTokenAdapter(keycloakTokenFeignClient);
    }
}
