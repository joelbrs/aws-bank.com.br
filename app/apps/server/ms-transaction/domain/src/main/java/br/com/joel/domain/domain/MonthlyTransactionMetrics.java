package br.com.joel.domain.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyTransactionMetrics {
    private Integer month;
    private BigDecimal sent;
    private BigDecimal received;
    private BigDecimal total;
}
