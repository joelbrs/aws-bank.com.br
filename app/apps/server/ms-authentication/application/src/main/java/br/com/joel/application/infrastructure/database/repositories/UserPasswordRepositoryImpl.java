package br.com.joel.application.infrastructure.database.repositories;

import br.com.joel.application.infrastructure.database.domain.JpaUserModel;
import br.com.joel.application.infrastructure.database.domain.JpaUserPasswordModel;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaUserPasswordRepository;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaUserRepository;
import br.com.joel.domain.domain.UserPassword;
import br.com.joel.ports.database.UserPasswordRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserPasswordRepositoryImpl implements UserPasswordRepository {

    private final JpaUserPasswordRepository jpaUserPasswordRepository;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public void create(UserPassword userPassword) {
        jpaUserPasswordRepository.save(this.toJpaDomain(userPassword));
    }

    @Override
    public Optional<UserPassword> getByTaxIdIfUserIsActive(String taxId) {
        Optional<JpaUserPasswordModel> userPassword = jpaUserPasswordRepository.getByTaxIdIfUserIsActive(taxId);
        return userPassword.map(this::toDomain);
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

    private UserPassword toDomain(JpaUserPasswordModel userPassword) {
        return UserPassword.builder()
                .taxId(userPassword.getUser().getTaxId())
                .loginPassword(userPassword.getLoginPassword())
                .actionsPassword(userPassword.getActionsPassword())
                .build();
    }
}
