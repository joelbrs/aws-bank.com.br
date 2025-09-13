package br.com.joel.application.infrastructure.adapters;

import br.com.joel.ports.CryptoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class CryptoBCryptAdapter implements CryptoPort {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String hash(String input) {
        return bCryptPasswordEncoder.encode(input);
    }
}
