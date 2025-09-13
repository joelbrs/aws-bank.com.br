package br.com.joel.domain.domain;

import br.com.joel.domain.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long senderAccountId;
    private Long recipientAccountId;
    private BigDecimal amount;
    private TransactionStatus status;
    private Balance provisionalBalance;
    private String description;
    private String idempotencyKey;

    @Builder.Default
    private Instant createdAt = Instant.now();
    private Instant updatedAt;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Balance {
        private BigDecimal amountSenderAccount;
        private BigDecimal amountRecipientAccount;
    }

    public void validate() {
        boolean isAmountValid = amount != null && amount.compareTo(java.math.BigDecimal.ZERO) > 0;
        boolean isSenderAccountValid = senderAccountId != null && senderAccountId > 0;
        boolean isRecipientAccountValid = recipientAccountId != null && recipientAccountId > 0;
        boolean isIdempotencyKeyValid = idempotencyKey != null && !idempotencyKey.isEmpty();
        boolean isBalanceValid = provisionalBalance != null
                && Objects.requireNonNull(provisionalBalance.getAmountSenderAccount()).compareTo(amount) >= 0;

        Map<String, String> fieldErrors = Map.of(
                "amount", "Amount must be greater than zero.",
                "senderAccountId", "Sender Account ID must be a positive number.",
                "recipientAccountId", "Recipient Account ID must be a positive number.",
                "idempotencyKey", "Idempotency Key must not be null or empty.",
                "balance", "Insufficient funds in sender's account."
        );

        List<String> errors = fieldErrors.entrySet().stream()
                .filter(entry ->
                    switch (entry.getKey()) {
                        case "amount" -> !isAmountValid;
                        case "senderAccountId" -> !isSenderAccountValid;
                        case "recipientAccountId" -> !isRecipientAccountValid;
                        case "idempotencyKey" -> !isIdempotencyKeyValid;
                        case "balance" -> !isBalanceValid;
                        default -> false;
                })
                .map(Map.Entry::getValue)
                .toList();

        if (!errors.isEmpty()) {
            //TODO: personalized exception
            throw new IllegalArgumentException("Invalid fields: " + String.join(", ", errors));
        }
    }

    public void updateProvisionalBalanceAfterTransaction() {
        BigDecimal newSenderBalance = provisionalBalance.getAmountSenderAccount().subtract(amount);
        BigDecimal newRecipientBalance = provisionalBalance.getAmountRecipientAccount().add(amount);

        this.provisionalBalance = Balance.builder()
                .amountSenderAccount(newSenderBalance)
                .amountRecipientAccount(newRecipientBalance)
                .build();
    }
}