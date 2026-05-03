package br.com.awsbank.app.authentication.domain.services.impl;

import br.com.awsbank.app.authentication.domain.models.Token;
import br.com.awsbank.app.authentication.domain.services.TokenService;
import br.com.awsbank.app.authentication.ports.out.TokenPort;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenPort tokenPort;

    private volatile String cachedToken;
    private volatile Instant expiry;

    public synchronized String getAccessToken() {
        if (cachedToken != null && expiry != null && Instant.now().isBefore(expiry)) {
            return cachedToken;
        }

        Token resp = tokenPort.getToken();
        if (resp == null || resp.getAccess_token() == null) {
            return null;
        }

        long expiresIn = resp.getExpires_in() != null ? resp.getExpires_in() : 60L;

        expiry = Instant.now().plusSeconds(Math.max(0, expiresIn - 5));
        cachedToken = resp.getAccess_token();
        return cachedToken;
    }
}
