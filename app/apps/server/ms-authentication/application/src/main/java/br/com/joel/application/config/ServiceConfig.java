package br.com.joel.application.config;

import br.com.joel.application.infrastructure.services.JwtAuthenticationService;
import br.com.joel.application.infrastructure.services.TOTPTransactionalService;
import br.com.joel.application.infrastructure.services.UserTransactionalService;
import br.com.joel.ports.EmailPort;
import br.com.joel.ports.crypto.CryptoPort;
import br.com.joel.ports.database.AccountRepository;
import br.com.joel.ports.database.UserPasswordRepository;
import br.com.joel.ports.database.UserRepository;
import br.com.joel.ports.database.cache.CacheRepository;
import br.com.joel.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    public static final String ACCOUNT_SERVICE = "AccountService";
    public static final String CRYPTO_SERVICE = "CryptoService";
    public static final String TOTP_SERVICE = "TOTPService";
    public static final String USER_PASSWORD_SERVICE = "UserPasswordService";
    public static final String USER_SERVICE = "UserService";
    public static final String AUTHENTICATION_SERVICE = "AuthenticationService";

    @Bean(name = ACCOUNT_SERVICE)
    public AccountService accountService(AccountRepository accountRepository) {
        return new AccountService(accountRepository);
    }

    @Bean(name = CRYPTO_SERVICE)
    public CryptoService cryptoService(CryptoPort cryptoPort) {
        return new CryptoService(cryptoPort);
    }

    @Bean(name = TOTP_SERVICE)
    public TOTPService totpService(EmailPort emailPort, CacheRepository cacheRepository) {
        return new TOTPTransactionalService(emailPort, cacheRepository);
    }

    @Bean(name = USER_PASSWORD_SERVICE)
    public UserPasswordService userPasswordService(
            UserPasswordRepository userPasswordRepository, CryptoService cryptoService
    ) {
        return new UserPasswordService(userPasswordRepository, cryptoService);
    }

    @Bean(name = USER_SERVICE)
    public UserService userService(
            UserRepository userRepository,
            UserPasswordService userPasswordService,
            TOTPService totpService,
            AccountService accountService
    ) {
        return new UserTransactionalService(userRepository, userPasswordService, totpService, accountService);
    }

    @Bean(name = AUTHENTICATION_SERVICE)
    public AuthenticationService authenticationService(
            UserService userService,
            UserPasswordService userPasswordService,
            CryptoService cryptoService,
            CacheRepository cacheRepository,
            @Value("${jwt.secret}") String jwtSecret
    ) {
        return new JwtAuthenticationService(
                userService,
                userPasswordService,
                cryptoService,
                cacheRepository,
                jwtSecret
        );
    }
}
