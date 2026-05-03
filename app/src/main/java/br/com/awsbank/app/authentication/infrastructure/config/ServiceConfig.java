package br.com.awsbank.app.authentication.infrastructure.config;

import br.com.awsbank.app.authentication.domain.services.AuthenticationService;
import br.com.awsbank.app.authentication.domain.services.TokenService;
import br.com.awsbank.app.authentication.domain.services.impl.AuthenticationServiceImpl;
import br.com.awsbank.app.authentication.domain.services.impl.TokenServiceImpl;
import br.com.awsbank.app.authentication.ports.out.IdentityProviderPort;
import br.com.awsbank.app.authentication.ports.out.TokenPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    AuthenticationService authenticationService(IdentityProviderPort identityProviderPort) {
        return new AuthenticationServiceImpl(identityProviderPort);
    }

    @Bean
    TokenService tokenService(TokenPort tokenPort) {
        return new TokenServiceImpl(tokenPort);
    }
}
