package br.com.joel.application.infrastructure.config;

import br.com.joel.application.infrastructure.database.repositories.TransactionRepositoryImpl;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaTransactionRepository;
import br.com.joel.ports.database.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = JpaTransactionRepository.class)
public class RepositoryConfig {

    public static final String TRANSACTION_REPOSITORY = "TransactionRepository";

    @Bean(name = TRANSACTION_REPOSITORY)
    public TransactionRepository transactionRepository(JpaTransactionRepository jpaTransactionRepository) {
        return new TransactionRepositoryImpl(jpaTransactionRepository);
    }
}
