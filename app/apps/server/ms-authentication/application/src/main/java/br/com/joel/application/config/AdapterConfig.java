package br.com.joel.application.config;

import br.com.joel.application.config.properties.SesProperties;
import br.com.joel.application.infrastructure.adapters.EmailSESAdapter;
import br.com.joel.ports.EmailPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

@Configuration
public class AdapterConfig {

    public static final String EMAIL_ADAPTER = "EmailAdapter";

    @Bean(name = EMAIL_ADAPTER)
    public EmailPort emailAdapter(SesProperties sesProperties, MailSender mailSender) {
        return new EmailSESAdapter(sesProperties, mailSender);
    }
}
