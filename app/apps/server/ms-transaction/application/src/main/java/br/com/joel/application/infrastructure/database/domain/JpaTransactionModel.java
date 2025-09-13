package br.com.joel.application.infrastructure.database.domain;

import br.com.joel.domain.domain.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_transactions")
public class JpaTransactionModel {

    @Id
    private String idempotencyKey;

    private Long senderAccountId;
    private Long recipientAccountId;
    private BigDecimal amount;
    private BigDecimal senderProvisionalBalance;
    private BigDecimal recipientProvisionalBalance;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private String description;

    private Instant createdAt;
    private Instant updatedAt;
}
