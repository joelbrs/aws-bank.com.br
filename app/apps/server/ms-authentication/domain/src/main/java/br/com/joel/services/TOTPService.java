package br.com.joel.services;

import br.com.joel.ports.EmailPort;
import br.com.joel.ports.database.cache.TOTPCacheRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TOTPService {

    private final EmailPort emailPort;
    private final TOTPCacheRepository totpCacheRepository;

    public void sendTOTP(String taxId, String to) {
        int totpCode = this.generateTOTP();

        String subject = "Your TOTP Code";
        String body = "Your TOTP code is: " + totpCode;

        totpCacheRepository.save(taxId, String.valueOf(totpCode));
        emailPort.send(to, subject, body);
    }

    public void validateTOTP(String taxId, String code) {
        String cachedCode = totpCacheRepository.get(taxId);

        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new IllegalArgumentException("Invalid or expired TOTP code.");
        }

        totpCacheRepository.delete(taxId);
    }

    private int generateTOTP() {
        return (int)(Math.random() * 900000) + 100000; // 6-digit code (random)
    }
}
