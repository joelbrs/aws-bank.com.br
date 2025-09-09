package br.com.joel.services;

import br.com.joel.domain.domain.User;
import br.com.joel.exceptions.ExternalServiceException;
import br.com.joel.ports.database.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final CacheRepository cacheRepository;
    private final CryptoService cryptoService;
    private final String jwtSecret;

    public void signUp(User user) {
        user.setTaxId(this.encryptTaxId(user.getTaxId()));
        userService.createUser(user);
    }

    public void confirmSignUp(String taxId, String totpCode) {
        userService.confirmUser(this.encryptTaxId(taxId), totpCode);
    }

    private String encryptTaxId(String taxId) {
        try {
            return cryptoService.encrypt(taxId, jwtSecret);
        } catch (Exception e) {
            log.error("[ERROR] Exception when encrypting taxId: {}", e.getMessage());
            throw new ExternalServiceException("Error encrypting taxId...", e);
        }
    }
}
