package br.com.joel.application.infrastructure.database.repositories;

import br.com.joel.application.infrastructure.database.domain.JpaTransactionModel;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaTransactionRepository;
import br.com.joel.domain.domain.MonthlyTransactionMetrics;
import br.com.joel.domain.domain.Transaction;
import br.com.joel.domain.domain.TransactionDetails;
import br.com.joel.ports.database.TransactionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JpaTransactionRepository jpaTransactionRepository;

    @Override
    public boolean idempotencyKeyExists(String idempotencyKey) {
        return jpaTransactionRepository.idempotencyKeyExists(idempotencyKey);
    }

    @Override
    public void save(Transaction transaction) {
        jpaTransactionRepository.save(this.toJpaDomain(transaction));
    }

    @Override
    public Transaction.Balance getCurrentValidProvisionalBalance(Long senderAccountId, Long recipientAccountId) {
        return jpaTransactionRepository.getCurrentValidProvisionalBalance(senderAccountId, recipientAccountId);
    }

    @Override
    public TransactionDetails getTransactionDetails(String idempontencyKey) {
        return jpaTransactionRepository.getTransactionDetails(idempontencyKey);
    }

    @Override
    public List<MonthlyTransactionMetrics> getMonthlyTransactionMetrics(Long accountId) {
        return jpaTransactionRepository.getMonthlyMetrics(accountId);
    }

    private JpaTransactionModel toJpaDomain(Transaction transaction) {
        return JpaTransactionModel.builder()
                .idempotencyKey(transaction.getIdempotencyKey())
                .senderAccountId(transaction.getSenderAccountId())
                .recipientAccountId(transaction.getRecipientAccountId())
                .amount(transaction.getAmount())
                .senderProvisionalBalance(transaction.getProvisionalBalance().getAmountSenderAccount())
                .recipientProvisionalBalance(transaction.getProvisionalBalance().getAmountRecipientAccount())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}
