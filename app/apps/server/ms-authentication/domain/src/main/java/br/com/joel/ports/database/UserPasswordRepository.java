package br.com.joel.ports.database;

import br.com.joel.domain.domain.UserPassword;

import java.util.Optional;

public interface UserPasswordRepository {
    void create(UserPassword userPassword);
    Optional<UserPassword> getByTaxIdIfUserIsActive(String taxId);
}
