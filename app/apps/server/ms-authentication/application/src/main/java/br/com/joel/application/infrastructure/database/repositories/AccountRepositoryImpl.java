package br.com.joel.application.infrastructure.database.repositories;

import br.com.joel.application.infrastructure.database.domain.JpaAccountModel;
import br.com.joel.application.infrastructure.database.domain.JpaUserModel;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaAccountRepository;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaUserRepository;
import br.com.joel.domain.domain.Account;
import br.com.joel.ports.database.AccountRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaAccountRepository jpaAccountRepository;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public void create(Account account) {
        jpaAccountRepository.save(this.toJpaDomain(account));
    }

    private JpaAccountModel toJpaDomain(Account account) {
        JpaUserModel user = jpaUserRepository.findById(account.getUserTaxId())
                .orElseThrow(() -> new IllegalArgumentException("User was not found"));

        return JpaAccountModel.builder()
                .accountNumber(account.getAccountNumber())
                .user(user)
                .build();
    }
}
