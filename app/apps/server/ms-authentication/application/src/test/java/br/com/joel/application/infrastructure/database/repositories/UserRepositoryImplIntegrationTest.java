package br.com.joel.application.infrastructure.database.repositories;

import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.enums.UserStatus;
import br.com.joel.ports.database.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryImplIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void deveSalvarERecuperarUsuario() {
        User user = User.builder()
                .taxId("123")
                .name("Nome")
                .lastName("Sobrenome")
                .email("email@teste.com")
                .build();

        userRepository.create(user);

        boolean encontrado = userRepository.existsByTaxId(user.getTaxId());
        Assertions.assertTrue(encontrado);
    }

    @Test
    void deveConfirmarUsuario() {
        User user = User.builder()
                .taxId("123")
                .name("Nome")
                .lastName("Sobrenome")
                .email("email@teste.com")
                .status(UserStatus.NOT_CONFIRMED)
                .build();

        userRepository.create(user);

        Assertions.assertDoesNotThrow(() -> userRepository.confirm(user.getTaxId()));
    }
}