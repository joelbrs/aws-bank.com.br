package br.com.joel.ports.database;

import br.com.joel.domain.domain.User;

import java.util.Optional;

public interface UserRepository {
    void create(User user);
    boolean existsByTaxId(String taxId);
    void confirm(String taxId);
    Optional<User> getByTaxId(String taxId);
}
