package br.com.joel.application.infrastructure.adapters;

import br.com.joel.ports.crypto.CryptoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class CryptoBcryptAdapter implements CryptoPort {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String hash(String value) {
        return bCryptPasswordEncoder.encode(value);
    }
}
