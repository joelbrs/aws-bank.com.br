package br.com.joel.ports.database;

import br.com.joel.domain.domain.Account;

public interface AccountRepository {
    void create(Account account);
}
