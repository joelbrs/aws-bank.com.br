package br.com.joel.application.infrastructure.services;

import br.com.joel.domain.domain.Token;
import br.com.joel.domain.domain.enums.TokenType;
import br.com.joel.exceptions.ExternalServiceException;
import br.com.joel.ports.database.cache.CacheRepository;
import br.com.joel.services.AuthenticationService;
import br.com.joel.services.CryptoService;
import br.com.joel.services.UserPasswordService;
import br.com.joel.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtAuthenticationService extends AuthenticationService {

    private final CryptoService cryptoService;
    private final String jwtSecret;

    public JwtAuthenticationService(UserService userService, UserPasswordService userPasswordService, CryptoService cryptoService, CacheRepository cacheRepository, String jwtSecret) {
        super(userService, userPasswordService, cryptoService, cacheRepository, jwtSecret);
        this.cryptoService = cryptoService;
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected Token generateBothTokens(String username) {
        String refreshToken = this.generateToken(username, TokenType.REFRESH_TOKEN);
        String accessToken = this.generateToken(username, TokenType.ACCESS_TOKEN);

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String getUsername(String token) {
        String encryptedCpf = Jwts.parserBuilder()
                .setSigningKey(this.getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        try {
            return cryptoService.decrypt(encryptedCpf, jwtSecret);
        } catch (Exception e) {
            log.error("[ERROR]: Error decrypting CPF from token", e);
            throw new RuntimeException("Error decrypting CPF from token", e);
        }
    }

    @Override
    public Date getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    private String generateToken(String cpf, TokenType tokenType) {
        try {
            Map<String, Object> claims = Map.of(
                    "type", tokenType.getClaimName()
            );

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(cryptoService.encrypt(cpf, jwtSecret))
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plusSeconds(tokenType.getExpiration())))
                    .signWith(this.getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("[ERROR]: Error generating {} for CPF {}", tokenType.name(), cpf, e);
            throw new ExternalServiceException("Error generating " + tokenType.name() + " for CPF " + cpf, e);
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
