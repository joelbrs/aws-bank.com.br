package br.com.joelf.domain.domain;

import br.com.joel.domain.domain.Transaction;
import br.com.joel.domain.domain.Transaction.Balance;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void validate_todosCamposValidos_naoLancaExcecao() {
        Transaction tx = Transaction.builder()
                .senderAccountId(1L)
                .recipientAccountId(2L)
                .amount(new BigDecimal("10.00"))
                .idempotencyKey("key")
                .provisionalBalance(Balance.builder()
                        .amountSenderAccount(new BigDecimal("100.00"))
                        .amountRecipientAccount(new BigDecimal("50.00"))
                        .build())
                .build();

        assertDoesNotThrow(tx::validate);
    }

    @Test
    void validate_valorZero_geraErro() {
        Transaction tx = Transaction.builder()
                .senderAccountId(1L)
                .recipientAccountId(2L)
                .amount(BigDecimal.ZERO)
                .idempotencyKey("key")
                .provisionalBalance(Balance.builder()
                        .amountSenderAccount(new BigDecimal("100.00"))
                        .amountRecipientAccount(new BigDecimal("50.00"))
                        .build())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, tx::validate);
        assertTrue(ex.getMessage().contains("Amount must be greater than zero"));
    }

    @Test
    void validate_saldoInsuficiente_geraErro() {
        Transaction tx = Transaction.builder()
                .senderAccountId(1L)
                .recipientAccountId(2L)
                .amount(new BigDecimal("200.00"))
                .idempotencyKey("key")
                .provisionalBalance(Balance.builder()
                        .amountSenderAccount(new BigDecimal("100.00"))
                        .amountRecipientAccount(new BigDecimal("50.00"))
                        .build())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, tx::validate);
        assertTrue(ex.getMessage().contains("Insufficient funds"));
    }

    @Test
    void validate_idempotencyKeyVazio_geraErro() {
        Transaction tx = Transaction.builder()
                .senderAccountId(1L)
                .recipientAccountId(2L)
                .amount(new BigDecimal("10.00"))
                .idempotencyKey("")
                .provisionalBalance(Balance.builder()
                        .amountSenderAccount(new BigDecimal("100.00"))
                        .amountRecipientAccount(new BigDecimal("50.00"))
                        .build())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, tx::validate);
        assertTrue(ex.getMessage().contains("Idempotency Key must not be null or empty"));
    }

    @Test
    void updateProvisionalBalanceAfterTransaction_atualizaSaldosCorretamente() {
        Balance balance = Balance.builder()
                .amountSenderAccount(new BigDecimal("100.00"))
                .amountRecipientAccount(new BigDecimal("50.00"))
                .build();

        Transaction tx = Transaction.builder()
                .senderAccountId(1L)
                .recipientAccountId(2L)
                .amount(new BigDecimal("10.00"))
                .provisionalBalance(balance)
                .build();

        tx.updateProvisionalBalanceAfterTransaction();

        assertEquals(new BigDecimal("90.00"), tx.getProvisionalBalance().getAmountSenderAccount());
        assertEquals(new BigDecimal("60.00"), tx.getProvisionalBalance().getAmountRecipientAccount());
    }

    @Test
    void validate_senderAccountIdInvalido_geraErro() {
        Transaction tx = Transaction.builder()
                .senderAccountId(0L)
                .recipientAccountId(2L)
                .amount(new BigDecimal("10.00"))
                .idempotencyKey("key")
                .provisionalBalance(Balance.builder()
                        .amountSenderAccount(new BigDecimal("100.00"))
                        .amountRecipientAccount(new BigDecimal("50.00"))
                        .build())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, tx::validate);
        assertTrue(ex.getMessage().contains("Sender Account ID must be a positive number"));
    }

    @Test
    void validate_recipientAccountIdInvalido_geraErro() {
        Transaction tx = Transaction.builder()
                .senderAccountId(1L)
                .recipientAccountId(-1L)
                .amount(new BigDecimal("10.00"))
                .idempotencyKey("key")
                .provisionalBalance(Balance.builder()
                        .amountSenderAccount(new BigDecimal("100.00"))
                        .amountRecipientAccount(new BigDecimal("50.00"))
                        .build())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, tx::validate);
        assertTrue(ex.getMessage().contains("Recipient Account ID must be a positive number"));
    }
}