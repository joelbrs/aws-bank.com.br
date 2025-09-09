package br.com.joel.application.infrastructure.database.repositories;

import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.UserPassword;
import br.com.joel.domain.domain.enums.UserStatus;
import br.com.joel.ports.database.UserPasswordRepository;
import br.com.joel.ports.database.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserPasswordRepositoryImplIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Test
    void deveSalvarERecuperarSenhaDoUsuario() {
        User user = User.builder()
                .taxId("123")
                .name("Nome")
                .lastName("Sobrenome")
                .email("email@teste.com")
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.create(user);

        UserPassword password = UserPassword.builder()
                .taxId(user.getTaxId())
                .loginPassword("senha123")
                .actionsPassword("acao456")
                .build();

        Assertions.assertDoesNotThrow(() -> userPasswordRepository.create(password));
    }
}
