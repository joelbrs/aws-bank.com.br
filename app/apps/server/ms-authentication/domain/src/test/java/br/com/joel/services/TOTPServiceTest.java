package br.com.joel.services;

import br.com.joel.ports.EmailPort;
import br.com.joel.ports.database.cache.TOTPCacheRepository;
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
    private TOTPCacheRepository totpCacheRepository;

    @InjectMocks
    private TOTPService totpService;

    @Test
    void sendTOTP_shouldSaveCodeAndSendEmail() {
        String taxId = "12345678901";
        String to = "user@email.com";

        doNothing().when(totpCacheRepository).save(eq(taxId), anyString());
        doNothing().when(emailPort).send(eq(to), anyString(), anyString());

        totpService.sendTOTP(taxId, to);

        verify(totpCacheRepository, times(1)).save(eq(taxId), anyString());
        verify(emailPort, times(1)).send(eq(to), eq("Your TOTP Code"), contains("Your TOTP code is:"));
    }

    @Test
    void validateTOTP_shouldDeleteIfCodeMatches() {
        String taxId = "12345678901";
        String code = "123456";

        when(totpCacheRepository.get(taxId)).thenReturn(code);
        doNothing().when(totpCacheRepository).delete(taxId);

        assertDoesNotThrow(() -> totpService.validateTOTP(taxId, code));
        verify(totpCacheRepository).delete(taxId);
    }

    @Test
    void validateTOTP_shouldThrowIfCodeNotFound() {
        String taxId = "12345678901";
        String code = "123456";

        when(totpCacheRepository.get(taxId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> totpService.validateTOTP(taxId, code));
        assertTrue(ex.getMessage().contains("Invalid or expired TOTP code."));
        verify(totpCacheRepository, never()).delete(anyString());
    }

    @Test
    void validateTOTP_shouldThrowIfCodeDoesNotMatch() {
        String taxId = "12345678901";
        String code = "123456";

        when(totpCacheRepository.get(taxId)).thenReturn("654321");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> totpService.validateTOTP(taxId, code));
        assertTrue(ex.getMessage().contains("Invalid or expired TOTP code."));
        verify(totpCacheRepository, never()).delete(anyString());
    }
}
