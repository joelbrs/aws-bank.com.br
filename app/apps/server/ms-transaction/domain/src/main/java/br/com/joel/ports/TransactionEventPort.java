package br.com.joel.ports;

import br.com.joel.domain.domain.Transaction;
import br.com.joel.domain.domain.TransactionExtractPayload;

public interface TransactionEventPort {
    void publish(Transaction transaction);
    void publishTransactionExtractRequest(TransactionExtractPayload transactionExtractPayload);
}
