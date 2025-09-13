package br.com.joel.ports.database;

import br.com.joel.domain.domain.Transaction;

public interface TransactionRepository {
    boolean idempotencyKeyExists(String idempotencyKey);
    void save(Transaction transaction);
    Transaction.Balance getCurrentValidProvisionalBalance(Long senderAccountId, Long recipientAccountId);
}
