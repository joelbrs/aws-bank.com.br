package br.com.joel.application.config;

import br.com.joel.application.config.properties.SesProperties;
import br.com.joel.application.infrastructure.adapters.CryptoBcryptAdapter;
import br.com.joel.application.infrastructure.adapters.EmailSESAdapter;
import br.com.joel.ports.EmailPort;
import br.com.joel.ports.crypto.CryptoPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdapterConfig {

    public static final String SES_EMAIL_ADAPTER = "SESEmailAdapter";
    public static final String BCRYPT_CRYPTO_ADAPTER = "BcryptCryptoAdapter";

    @Bean(name = SES_EMAIL_ADAPTER)
    public EmailPort emailAdapter(SesProperties sesProperties, MailSender mailSender) {
        return new EmailSESAdapter(sesProperties, mailSender);
    }

    @Bean(name = BCRYPT_CRYPTO_ADAPTER)
    public CryptoPort cryptoAdapter(BCryptPasswordEncoder bCryptPasswordEncoder ) {
        return new CryptoBcryptAdapter(bCryptPasswordEncoder);
    }
}
