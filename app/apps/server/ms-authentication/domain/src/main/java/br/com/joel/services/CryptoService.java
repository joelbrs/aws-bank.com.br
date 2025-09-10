package br.com.joel.services;

import br.com.joel.ports.crypto.CryptoPort;
import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@RequiredArgsConstructor
public class CryptoService {

    private static final String ALGORITHM = "AES";

    private final CryptoPort cryptoPort;

    public String encrypt(String value, String secret) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, this.getKey(secret));
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encrypted, String secret) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, this.getKey(secret));
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(original);
    }

    public String hash(String value) {
        return cryptoPort.hash(value);
    }

    public boolean verifyHash(String value, String hash) {
        return cryptoPort.verify(value, hash);
    }

    private SecretKeySpec getKey(String secret) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, ALGORITHM);
    }
}
