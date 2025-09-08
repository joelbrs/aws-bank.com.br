package br.com.joel.services;

import br.com.joel.ports.crypto.CryptoPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CryptoServiceTest {

    @Mock
    private CryptoPort cryptoPort;

    @InjectMocks
    private CryptoService cryptoService;

    @Test
    void encrypt_and_decrypt() throws Exception {
        String secret = "chave";
        String original = "12345678901";
        String encrypted = cryptoService.encrypt(original, secret);
        String decrypted = cryptoService.decrypt(encrypted, secret);

        assertEquals(original, decrypted);
    }

    @Test
    void hash_and_verify() {
        String password = "senha";
        String hash = cryptoService.hash(password);

        assertNotEquals(password, hash);
    }
}
