package br.com.joel.services;

import br.com.joel.domain.domain.Account;
import br.com.joel.ports.database.AccountRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void createAccount(String userTaxId) {
        accountRepository.create(Account.createNewAccount(userTaxId));
    }
}
