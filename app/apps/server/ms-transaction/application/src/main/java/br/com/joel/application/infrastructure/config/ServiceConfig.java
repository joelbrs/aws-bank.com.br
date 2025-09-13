package br.com.joel.application.infrastructure.config;

import br.com.joel.application.infrastructure.services.TransactionTransactionalService;
import br.com.joel.ports.CryptoPort;
import br.com.joel.ports.TransactionAuthorizerPort;
import br.com.joel.ports.TransactionEventPort;
import br.com.joel.ports.database.TransactionRepository;
import br.com.joel.services.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    public static final String TRANSACTION_SERVICE = "TransactionService";

    @Bean(name = TRANSACTION_SERVICE)
    public TransactionService transactionService(
            CryptoPort cryptoPort,
            TransactionEventPort transactionEventPort,
            TransactionRepository transactionRepository,
            TransactionAuthorizerPort transactionAuthorizerPort
    ) {
        return new TransactionTransactionalService(cryptoPort, transactionEventPort, transactionRepository, transactionAuthorizerPort);
    }
}
