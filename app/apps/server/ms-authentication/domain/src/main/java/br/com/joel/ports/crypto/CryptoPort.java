package br.com.joel.ports.crypto;

public interface CryptoPort {
    String hash(String value);
    boolean verify(String value, String secret);
}
