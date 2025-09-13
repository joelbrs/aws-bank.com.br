package br.com.joel.application.infrastructure.services;

import br.com.joel.domain.domain.Transaction;
import br.com.joel.ports.CryptoPort;
import br.com.joel.ports.TransactionAuthorizerPort;
import br.com.joel.ports.TransactionEventPort;
import br.com.joel.ports.database.TransactionRepository;
import br.com.joel.services.TransactionService;
import org.springframework.transaction.annotation.Transactional;

public class TransactionTransactionalService extends TransactionService {
    public TransactionTransactionalService(
            CryptoPort cryptoPort,
            TransactionEventPort transactionEventPort,
            TransactionRepository transactionRepository,
            TransactionAuthorizerPort transactionAuthorizerPort
    ) {
        super(cryptoPort, transactionEventPort, transactionRepository, transactionAuthorizerPort);
    }

    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        return super.createTransaction(transaction);
    }
}
