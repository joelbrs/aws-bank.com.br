package br.com.joel.application.infrastructure.database.repositories;

import br.com.joel.application.infrastructure.database.domain.JpaUserModel;
import br.com.joel.application.infrastructure.database.domain.JpaUserPasswordModel;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaUserPasswordRepository;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaUserRepository;
import br.com.joel.domain.domain.UserPassword;
import br.com.joel.ports.database.UserPasswordRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserPasswordRepositoryImpl implements UserPasswordRepository {

    private final JpaUserPasswordRepository jpaUserPasswordRepository;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public void create(UserPassword userPassword) {
        jpaUserPasswordRepository.save(this.toJpaDomain(userPassword));
    }

    private JpaUserPasswordModel toJpaDomain(UserPassword userPassword) {
        JpaUserModel user = jpaUserRepository.findById(userPassword.getTaxId())
                .orElseThrow(() -> new IllegalArgumentException("User was not found"));

        return JpaUserPasswordModel.builder()
                .user(user)
                .loginPassword(userPassword.getLoginPassword())
                .actionsPassword(userPassword.getActionsPassword())
                .build();
    }
}
