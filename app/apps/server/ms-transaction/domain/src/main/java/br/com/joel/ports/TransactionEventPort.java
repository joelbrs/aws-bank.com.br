package br.com.joel.ports;

import br.com.joel.domain.domain.Transaction;

public interface TransactionEventPort {
    Transaction publish(Transaction transaction);
}
