package br.com.joel.application.config;

import br.com.joel.application.infrastructure.database.repositories.AccountRepositoryImpl;
import br.com.joel.application.infrastructure.database.repositories.UserPasswordRepositoryImpl;
import br.com.joel.application.infrastructure.database.repositories.UserRepositoryImpl;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaAccountRepository;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaUserPasswordRepository;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaUserRepository;
import br.com.joel.ports.database.AccountRepository;
import br.com.joel.ports.database.UserPasswordRepository;
import br.com.joel.ports.database.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.joel.application.infrastructure.database.repositories.framework")
public class RepositoryConfig {

    public static final String USER_REPOSITORY = "UserRepository";
    public static final String USER_PASSWORD_REPOSITORY = "UserPasswordRepository";
    public static final String ACCOUNT_REPOSITORY = "AccountRepository";

    @Bean(name = USER_REPOSITORY)
    public UserRepository userRepository(JpaUserRepository jpaUserRepository) {
        return new UserRepositoryImpl(jpaUserRepository);
    }

    @Bean(name = USER_PASSWORD_REPOSITORY)
    public UserPasswordRepository userPasswordRepository(
            JpaUserPasswordRepository jpaUserPasswordRepository, JpaUserRepository jpaUserRepository) {
        return new UserPasswordRepositoryImpl(jpaUserPasswordRepository, jpaUserRepository);
    }

    @Bean(name = ACCOUNT_REPOSITORY)
    public AccountRepository accountRepository(
            JpaAccountRepository accountRepository, JpaUserRepository jpaUserRepository
    ) {
        return new AccountRepositoryImpl(accountRepository, jpaUserRepository);
    }
}
