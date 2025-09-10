package br.com.joel.services;

import br.com.joel.domain.domain.Token;
import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.UserPassword;
import br.com.joel.domain.domain.enums.TokenType;
import br.com.joel.exceptions.BusinessException;
import br.com.joel.exceptions.ExternalServiceException;
import br.com.joel.ports.database.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public abstract class AuthenticationService {

    private final UserService userService;
    private final UserPasswordService userPasswordService;
    private final CryptoService cryptoService;
    private final CacheRepository cacheRepository;
    private final String jwtSecret;

    protected abstract Token generateBothTokens(String username);

    public abstract String getUsername(String token);
    public abstract Date getExpiration(String token);

    public void signUp(User user) {
        user.setTaxId(this.encryptTaxId(user.getTaxId()));
        userService.createUser(user);
    }

    public void confirmSignUp(String taxId, String totpCode) {
        userService.confirmUser(this.encryptTaxId(taxId), totpCode);
    }

    public Token signIn(String taxId, String password) {
        UserPassword userPassword = userPasswordService.getByTaxIdIfUserIsActive(this.encryptTaxId(taxId));

        if (!cryptoService.verifyHash(password, userPassword.getLoginPassword())) {
            throw new BusinessException("Invalid login password.");
        }

        try {
            return this.generateBothTokens(taxId);
        } catch (Exception e) {
            log.error("[ERROR] Exception when signing in: {}", e.getMessage());
            throw new ExternalServiceException("Error signing in...", e);
        }
    }

    public Token refreshToken(String currentRefreshToken) {
        String username = this.getUsername(currentRefreshToken);
        this.validateRefreshToken(username, currentRefreshToken);

        try {
            userService.getByTaxId(username);

            Token tokens = this.generateBothTokens(username);

            this.invalidateRefreshToken(username);
            this.saveRefreshTokenInCache(username, tokens.getRefreshToken());

            return tokens;
        } catch (Exception e) {
            log.error("[ERROR]: Could not refresh token: {}", username, e);
            throw new BusinessException("Could not refresh token: " + username, e);
        }
    }

    public boolean isTokenValid(String cpf, String token) {
        try {
            userService.getByTaxId(this.encryptTaxId(cpf));
        } catch (Exception e) {
            throw new BusinessException("[ERROR]: Username not found.", e);
        }
        return !isTokenExpired(token);
    }

    private String encryptTaxId(String taxId) {
        try {
            return cryptoService.encrypt(taxId, jwtSecret);
        } catch (Exception e) {
            log.error("[ERROR] Exception when encrypting taxId: {}", e.getMessage());
            throw new ExternalServiceException("Error encrypting taxId...", e);
        }
    }

    private void validateRefreshToken(String username, String refreshToken) {
        try {
            username = cryptoService.decrypt(username, jwtSecret);
        } catch (Exception e) {
            log.error("[ERROR] Exception when decrypt username: {}", e.getMessage());
            throw new ExternalServiceException("Error decrypt username...", e);
        }

        String refreshTokenFromCache = String.valueOf(cacheRepository.get(this.getCacheKey(username)));

        if (refreshTokenFromCache != null && !refreshTokenFromCache.equals(refreshToken)) {
            log.error("[ERROR]: Refresh token is invalid for user: {}", username);
            throw new BusinessException("[ERROR]: Refresh token is invalid");
        }

        if (isTokenExpired(refreshToken)) {
            throw new BusinessException("[ERROR]: Refresh token is expired");
        }
    }

    private void invalidateRefreshToken(String username) throws Exception {
        username = cryptoService.decrypt(username, jwtSecret);
        cacheRepository.delete(this.getCacheKey(username));
    }

    private void saveRefreshTokenInCache(String username, String refreshToken) throws Exception {
        username = cryptoService.encrypt(username, jwtSecret);
        cacheRepository.save(this.getCacheKey(username), refreshToken);
    }

    private Boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private String getCacheKey(String taxId) {
        return TokenType.ACCESS_TOKEN.getClaimName().concat(taxId);
    }
}
