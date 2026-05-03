package br.com.awsbank.app.authentication.adapters.out.rest.client.config;

import br.com.awsbank.app.authentication.domain.services.TokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakFeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(TokenService tokenService) {
        return template -> {
            String token = tokenService.getAccessToken();

            if (token != null && !token.isBlank()) {
                template.header("Authorization", "Bearer " + token);
            }
        };
    }
}
