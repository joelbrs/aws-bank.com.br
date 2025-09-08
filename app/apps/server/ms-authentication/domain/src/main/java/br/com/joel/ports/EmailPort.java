package br.com.joel.ports;

public interface EmailPort {
    void send(String to, String subject, String body);
}
