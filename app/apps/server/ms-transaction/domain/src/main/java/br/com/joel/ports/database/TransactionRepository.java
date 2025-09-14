package br.com.joel.ports.database;

import br.com.joel.domain.domain.MonthlyTransactionMetrics;
import br.com.joel.domain.domain.Transaction;
import br.com.joel.domain.domain.TransactionDetails;

import java.util.List;

public interface TransactionRepository {
    boolean idempotencyKeyExists(String idempotencyKey);
    void save(Transaction transaction);
    Transaction.Balance getCurrentValidProvisionalBalance(Long senderAccountId, Long recipientAccountId);
    TransactionDetails getTransactionDetails(String idempontencyKey);
    List<MonthlyTransactionMetrics> getMonthlyTransactionMetrics(Long accountId);
}
