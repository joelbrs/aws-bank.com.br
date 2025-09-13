package br.com.joelf.services;

import br.com.joel.domain.domain.Transaction;
import br.com.joel.domain.domain.Transaction.Balance;
import br.com.joel.domain.domain.enums.TransactionStatus;
import br.com.joel.ports.CryptoPort;
import br.com.joel.ports.TransactionAuthorizerPort;
import br.com.joel.ports.TransactionEventPort;
import br.com.joel.ports.database.TransactionRepository;
import br.com.joel.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CryptoPort cryptoPort;
    @Mock
    private TransactionEventPort transactionEventPort;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionAuthorizerPort transactionAuthorizerPort;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private Balance balance;

    @BeforeEach
    void setUp() {
        balance = Balance.builder()
                .amountSenderAccount(new BigDecimal("100.00"))
                .amountRecipientAccount(new BigDecimal("50.00"))
                .build();

        transaction = Transaction.builder()
                .senderAccountId(1L)
                .recipientAccountId(2L)
                .amount(new BigDecimal("10.00"))
                .idempotencyKey("idem-key")
                .status(TransactionStatus.PENDING_PROCESSING)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void createTransaction_sucesso() {
        when(transactionRepository.getCurrentValidProvisionalBalance(1L, 2L)).thenReturn(balance);
        when(transactionRepository.idempotencyKeyExists("idem-key")).thenReturn(false);

        Transaction result = transactionService.createTransaction(transaction);

        assertEquals(TransactionStatus.PENDING_PROCESSING, result.getStatus());
        verify(transactionRepository).save(transaction);
        verify(transactionEventPort).publish(transaction);
    }

    @Test
    void createTransaction_idempotencyKeyExistente_lancaExcecao() {
        when(transactionRepository.getCurrentValidProvisionalBalance(1L, 2L)).thenReturn(balance);
        when(transactionRepository.idempotencyKeyExists("idem-key")).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> transactionService.createTransaction(transaction));
    }

    @Test
    void generateIdempotencyKey_parametrosValidos_retornaChave() {
        when(cryptoPort.hash(anyString())).thenReturn("hash");
        String key = transactionService.generateIdempotencyKey(1L, 2L, new BigDecimal("10.00"), Instant.EPOCH);
        assertNotNull(key);
        assertTrue(key.contains("10.00"));
    }

    @Test
    void generateIdempotencyKey_parametroNulo_lancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.generateIdempotencyKey(null, 2L, BigDecimal.ONE, Instant.now()));
    }

    @Test
    void processTransactionEvent_sucesso() {
        transaction.setProvisionalBalance(balance);

        doNothing().when(transactionAuthorizerPort).authorize();

        transactionService.processTransactionEvent(transaction);

        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        verify(transactionRepository).save(transaction);
        verify(transactionAuthorizerPort).authorize();
    }

    @Test
    void processTransactionEvent_excecaoNaAutorizacao_statusFailed() {
        transaction.setProvisionalBalance(balance);

        doThrow(new RuntimeException("Erro")).when(transactionAuthorizerPort).authorize();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transactionService.processTransactionEvent(transaction));
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        verify(transactionRepository).save(transaction);
    }
}