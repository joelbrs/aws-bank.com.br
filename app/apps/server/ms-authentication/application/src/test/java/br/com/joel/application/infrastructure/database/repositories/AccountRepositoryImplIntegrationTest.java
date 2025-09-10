package br.com.joel.application.infrastructure.database.repositories;

import br.com.joel.domain.domain.Account;
import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.enums.UserStatus;
import br.com.joel.ports.database.AccountRepository;
import br.com.joel.ports.database.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
class AccountRepositoryImplIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void deveSalvarERecuperarConta() {
        User user = User.builder()
                .taxId("321")
                .name("Conta")
                .lastName("Teste")
                .email("conta@teste.com")
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.create(user);

        Assertions.assertDoesNotThrow(() -> accountRepository.create(Account.createNewAccount(user.getTaxId())));
    }
}