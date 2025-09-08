package br.com.joel.domain.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Builder
public class Account {
    private String userTaxId;
    private Long accountNumber;

    @Builder.Default
    private Instant updatedAt = Instant.now();

    private Instant createdAt;

    public static Account createNewAccount(String userTaxId) {
        return Account.builder()
                .userTaxId(userTaxId)
                .accountNumber(generateAccountNumber())
                .createdAt(Instant.now())
                .build();
    }

    private static Long generateAccountNumber() {
        return ThreadLocalRandom.current().nextLong(100000L, 999999L);
    }
}