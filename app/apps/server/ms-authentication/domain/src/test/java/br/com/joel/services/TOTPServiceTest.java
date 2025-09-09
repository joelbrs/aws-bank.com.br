package br.com.joel.services;

import br.com.joel.ports.EmailPort;
import br.com.joel.ports.database.cache.CacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TOTPServiceTest {

    @Mock
    private EmailPort emailPort;
    @Mock
    private CacheRepository cacheRepository;

    @InjectMocks
    private TOTPService totpService;

    @Test
    void sendTOTP_shouldSaveCodeAndSendEmail() {
        String taxId = "12345678901";
        String to = "user@email.com";

        doNothing().when(cacheRepository).save(eq(taxId), anyString());
        doNothing().when(emailPort).send(eq(to), anyString(), anyString());

        totpService.sendTOTP(taxId, to);

        verify(cacheRepository, times(1)).save(eq(taxId), anyString());
        verify(emailPort, times(1)).send(eq(to), eq("Your TOTP Code"), contains("Your TOTP code is:"));
    }

    @Test
    void validateTOTP_shouldDeleteIfCodeMatches() {
        String taxId = "12345678901";
        String code = "123456";

        when(cacheRepository.get(taxId)).thenReturn(code);
        doNothing().when(cacheRepository).delete(taxId);

        assertDoesNotThrow(() -> totpService.validateTOTP(taxId, code));
        verify(cacheRepository).delete(taxId);
    }

    @Test
    void validateTOTP_shouldThrowIfCodeNotFound() {
        String taxId = "12345678901";
        String code = "123456";

        when(cacheRepository.get(taxId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> totpService.validateTOTP(taxId, code));
        assertTrue(ex.getMessage().contains("Invalid or expired TOTP code."));
        verify(cacheRepository, never()).delete(anyString());
    }

    @Test
    void validateTOTP_shouldThrowIfCodeDoesNotMatch() {
        String taxId = "12345678901";
        String code = "123456";

        when(cacheRepository.get(taxId)).thenReturn("654321");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> totpService.validateTOTP(taxId, code));
        assertTrue(ex.getMessage().contains("Invalid or expired TOTP code."));
        verify(cacheRepository, never()).delete(anyString());
    }
}
