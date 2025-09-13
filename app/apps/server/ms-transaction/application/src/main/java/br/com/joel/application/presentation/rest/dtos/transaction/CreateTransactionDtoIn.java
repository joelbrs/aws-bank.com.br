package br.com.joel.application.presentation.rest.dtos.transaction;

import br.com.joel.domain.domain.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateTransactionDtoIn (

        @NotNull
        Long senderAccountId,

        @NotNull
        Long recipientAccountId,

        @NotNull
        @Positive
        BigDecimal amount,

        String description,

        @NotBlank
        String idempotencyKey,

        @NotNull
        Instant timestamp
) {
    public Transaction toDomain() {
        return Transaction.builder()
                .createdAt(timestamp)
                .senderAccountId(senderAccountId)
                .recipientAccountId(recipientAccountId)
                .amount(amount)
                .description(description)
                .idempotencyKey(idempotencyKey)
                .build();
    }
}
