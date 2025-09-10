package br.com.joel.application.infrastructure.adapters;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CryptoBcryptAdapterTest {

    @Test
    void hash_deveRetornarHashGeradoPeloBCrypt() {
        String valor = "senha123";
        String hashEsperado = "$2a$10$abcdefg";

        BCryptPasswordEncoder encoderMock = mock(BCryptPasswordEncoder.class);
        when(encoderMock.encode(valor)).thenReturn(hashEsperado);

        CryptoBcryptAdapter adapter = new CryptoBcryptAdapter(encoderMock);

        String hashGerado = adapter.hash(valor);

        assertEquals(hashEsperado, hashGerado);
        verify(encoderMock, times(1)).encode(valor);
    }
}