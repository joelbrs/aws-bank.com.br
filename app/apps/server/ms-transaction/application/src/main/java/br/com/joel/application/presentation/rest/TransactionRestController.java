package br.com.joel.application.presentation.rest;

import br.com.joel.application.presentation.rest.dtos.transaction.CreateTransactionDtoIn;
import br.com.joel.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;

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
}
