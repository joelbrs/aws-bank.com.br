package br.com.joel.application.infrastructure.database.repositories.framework;

import br.com.joel.application.infrastructure.database.domain.JpaTransactionModel;
import br.com.joel.domain.domain.Transaction;
import br.com.joel.domain.domain.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTransactionRepository extends JpaRepository<JpaTransactionModel, String> {

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM JpaTransactionModel t WHERE t.idempotencyKey = :idempotencyKey")
    boolean idempotencyKeyExists(String idempotencyKey);

    @Query("SELECT new br.com.joel.domain.domain.Transaction.Balance(" +
           "MAX(CASE WHEN t.senderAccountId = :senderAccountId THEN t.senderProvisionalBalance ELSE 0 END), " +
           "MAX(CASE WHEN t.recipientAccountId = :recipientAccountId THEN t.recipientProvisionalBalance ELSE 0 END)) " +
           "FROM JpaTransactionModel t " +
           "WHERE (t.senderAccountId = :senderAccountId OR t.recipientAccountId = :recipientAccountId) " +
           "AND t.status = 'COMPLETED'")
    Transaction.Balance getCurrentValidProvisionalBalance(Long senderAccountId, Long recipientAccountId);

    @Query("SELECT new br.com.joel.domain.domain.TransactionDetails(" +
            "t.senderAccountId, " +
            "t.recipientAccountId, " +
            "t.amount, " +
            "t.status, " +
            "t.description, " +
            "t.idempotencyKey, " +
            "t.createdAt, " +
            "t.updatedAt) " +
            "FROM JpaTransactionModel t " +
            "WHERE t.idempotencyKey = :idempotencyKey")
    TransactionDetails getTransactionDetails(String idempotencyKey);
}
