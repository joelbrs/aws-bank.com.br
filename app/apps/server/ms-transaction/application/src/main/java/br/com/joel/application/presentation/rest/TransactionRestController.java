package br.com.joel.application.presentation.rest;

import br.com.joel.application.presentation.rest.dtos.transaction.CreateTransactionDtoIn;
import br.com.joel.application.presentation.rest.dtos.transaction.RequestTransactionExtractDtoIn;
import br.com.joel.domain.domain.MonthlyTransactionMetrics;
import br.com.joel.domain.domain.TransactionDetails;
import br.com.joel.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionRestController {

    private final TransactionService transactionService;

    @PostMapping("/v1")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody @Valid CreateTransactionDtoIn dto) {
        transactionService.createTransaction(dto.toDomain());
    }

    @GetMapping("/v1/idempotency-key")
    @ResponseStatus(HttpStatus.OK)
    public String generateIdempotencyKey(
            @RequestParam Long senderAccountId,
            @RequestParam Long recipientAccountId,
            @RequestParam BigDecimal amount,
            @RequestParam Instant timestamp) {
        return transactionService.generateIdempotencyKey(
                senderAccountId,
                recipientAccountId,
                amount,
                timestamp
        );
    }

    @PostMapping("/v1/extract")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransactionExtractRequest(@RequestBody @Valid RequestTransactionExtractDtoIn dto) {
        transactionService.requestsExtract(dto.toDomain());
    }

    @GetMapping("/v1/{idempotencyKey}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionDetails getTransactionDetails(@PathVariable String idempotencyKey) {
        return transactionService.getTransactionDetails(idempotencyKey);
    }

    @GetMapping("/v1/metrics/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MonthlyTransactionMetrics> getMonthlyTransactionMetrics(@PathVariable Long accountId) {
        return transactionService.getMonthlyTransactionMetrics(accountId);
    }
}
