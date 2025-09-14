package br.com.joel.application.presentation.rest.dtos.transaction;

import br.com.joel.domain.domain.TransactionExtractPayload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Instant;

public record RequestTransactionExtractDtoIn(

        @NotNull
        Long accountId,

        @NotNull
        @PastOrPresent
        Instant startDate,

        @NotNull
        @PastOrPresent
        Instant endDate
) {
    public TransactionExtractPayload toDomain() {
        return TransactionExtractPayload.builder()
                .accountId(accountId)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
