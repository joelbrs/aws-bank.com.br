package br.com.joel.ports.database;

import br.com.joel.domain.domain.UserPassword;

public interface UserPasswordRepository {
    void create(UserPassword userPassword);
}
