package br.com.awsbank.app.authentication.infrastructure.config;

import br.com.awsbank.app.authentication.adapters.in.AuthenticationControllerAdapter;
import br.com.awsbank.app.authentication.domain.services.AuthenticationService;
import br.com.awsbank.app.authentication.ports.in.AuthenticationRestPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

    @Bean
    AuthenticationRestPort authenticationRestPort(AuthenticationService authenticationService) {
        return new AuthenticationControllerAdapter(authenticationService);
    }
}
