package br.com.joel.domain.domain;

import br.com.joel.domain.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetails {
    private Long senderAccountId;
    private Long recipientAccountId;
    private BigDecimal amount;
    private TransactionStatus status;
    private String description;
    private String idempotencyKey;
    private Instant createdAt;
    private Instant updatedAt;
}
