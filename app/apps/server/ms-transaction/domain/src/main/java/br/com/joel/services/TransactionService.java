package br.com.joel.services;

import br.com.joel.domain.domain.Transaction;
import br.com.joel.domain.domain.TransactionExtractPayload;
import br.com.joel.domain.domain.enums.TransactionStatus;
import br.com.joel.exceptions.BusinessException;
import br.com.joel.exceptions.ExternalServiceException;
import br.com.joel.ports.CryptoPort;
import br.com.joel.ports.TransactionAuthorizerPort;
import br.com.joel.ports.TransactionEventPort;
import br.com.joel.ports.database.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final CryptoPort cryptoPort;
    private final TransactionEventPort transactionEventPort;
    private final TransactionRepository transactionRepository;
    private final TransactionAuthorizerPort transactionAuthorizerPort;

    public Transaction createTransaction(Transaction transaction) {
        try {
            log.info("Creating transaction from account: {}", transaction.getRecipientAccountId());
            Transaction.Balance currentProvisionalBalance =
                    transactionRepository.getCurrentValidProvisionalBalance(transaction.getSenderAccountId(), transaction.getRecipientAccountId());

            transaction.setProvisionalBalance(currentProvisionalBalance);
            transaction.validate();

            this.idempotencyCheck(transaction.getIdempotencyKey());

            transaction.updateProvisionalBalanceBeforeTransaction();
            transaction.setUpdatedAt(transaction.getCreatedAt());
            transaction.setStatus(TransactionStatus.PENDING_PROCESSING);

            transactionRepository.save(transaction);
            transactionEventPort.publish(transaction);

            log.info("Transaction created successfully from account: {}", transaction.getRecipientAccountId());

            return transaction;
        } catch (BusinessException e) {
            log.error("Error processing transaction from account: {}", transaction.getRecipientAccountId(), e);
            throw new BusinessException("Error processing transaction from account: " + transaction.getRecipientAccountId(), e);
        } catch (Exception e) {
            log.error("Unexpected error processing transaction from account: {}", transaction.getRecipientAccountId(), e);
            throw new ExternalServiceException("Unexpected error processing transaction from account: " + transaction.getRecipientAccountId(), e);
        }
    }

    public String generateIdempotencyKey(Long senderAccountId, Long recipientAccountId, BigDecimal amount, Instant timestamp) {
        if (senderAccountId == null || recipientAccountId == null || amount == null || timestamp == null) {
            throw new IllegalArgumentException("All parameters must be provided and non-null");
        }

        String recipientAccountHash = cryptoPort.hash(recipientAccountId.toString());
        String senderAccountHash = cryptoPort.hash(senderAccountId.toString());

        return String.format("%s-%s-%s-%s",
                senderAccountHash,
                recipientAccountHash,
                amount,
                timestamp
        );
    }

    public void processTransactionEvent(Transaction transaction) {
        try {
            log.info("Processing transaction event for transaction ID: {}", transaction.getIdempotencyKey());

            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.updateProvisionalBalanceAfterTransaction();

            transactionAuthorizerPort.authorize();

            log.info("Transaction event processed successfully for transaction ID: {}", transaction.getIdempotencyKey());
        } catch (Exception e) {
            transaction.restoreProvisionalBalance();
            transaction.setStatus(TransactionStatus.FAILED);
            log.error("Error processing transaction event for transaction ID: {}", transaction.getIdempotencyKey(), e);
            throw new ExternalServiceException("Error processing transaction event for transaction ID: " + transaction.getIdempotencyKey(), e);
        } finally {
            transactionRepository.save(transaction);
        }
    }

    public void requestsExtract(TransactionExtractPayload transactionExtractPayload) {
        transactionEventPort.publishTransactionExtractRequest(transactionExtractPayload);
    }

    private void idempotencyCheck(String idempotencyKey) {
        boolean idempotencyKeyExists = transactionRepository.idempotencyKeyExists(idempotencyKey);

        if (idempotencyKeyExists) {
            log.error("Idempotency key already exists");
            throw new BusinessException(String.format("Idempotency key %s exists", idempotencyKey));
        }
        log.info("Idempotency key is valid");
    }
}
