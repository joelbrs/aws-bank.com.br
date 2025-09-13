package br.com.joel.domain.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionStatus {
    PENDING_PROCESSING(1L, "Pending Processing"),
    COMPLETED(2L, "Completed"),
    FAILED(3L, "Failed");

    private final Long code;
    private final String description;
}
