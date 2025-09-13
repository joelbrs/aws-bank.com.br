package br.com.joel.ports;

import br.com.joel.domain.domain.Transaction;

public interface TransactionEventPort {
    void publish(Transaction transaction);
}
