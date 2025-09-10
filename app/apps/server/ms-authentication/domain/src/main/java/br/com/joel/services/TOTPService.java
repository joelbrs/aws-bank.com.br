package br.com.joel.services;

import br.com.joel.ports.EmailPort;
import br.com.joel.ports.database.cache.CacheRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TOTPService {

    private final EmailPort emailPort;
    private final CacheRepository cacheRepository;

    public void sendTOTP(String taxId, String to) {
        int totpCode = this.generateTOTP();

        String subject = "Your TOTP Code";
        String body = "Your TOTP code is: " + totpCode;

        cacheRepository.save(taxId, String.valueOf(totpCode));
        emailPort.send(to, subject, body);
    }

    public void validateTOTP(String taxId, String code) {
        String cachedCode = cacheRepository.get(taxId);

        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new IllegalArgumentException("Invalid or expired TOTP code.");
        }

        cacheRepository.delete(taxId);
    }

    private int generateTOTP() {
        return (int)(Math.random() * 900000) + 100000; // 6-digit code (random)
    }
}
