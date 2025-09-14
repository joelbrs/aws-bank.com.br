package br.com.joel.domain.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionExtractPayload {
    private Long accountId;
    private Instant startDate;
    private Instant endDate;
}
