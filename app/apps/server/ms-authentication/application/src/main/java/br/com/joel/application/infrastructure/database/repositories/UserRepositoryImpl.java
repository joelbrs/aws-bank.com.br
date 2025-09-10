package br.com.joel.application.infrastructure.database.repositories;

import br.com.joel.application.infrastructure.database.domain.JpaUserModel;
import br.com.joel.application.infrastructure.database.repositories.framework.JpaUserRepository;
import br.com.joel.domain.domain.User;
import br.com.joel.ports.database.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void create(User user) {
        jpaUserRepository.save(this.toJpaDomain(user));
    }

    @Override
    public boolean existsByTaxId(String taxId) {
        return jpaUserRepository.existsById(taxId);
    }

    @Override
    public void confirm(String taxId) {
        jpaUserRepository.confirm(taxId);
    }

    @Override
    public Optional<User> getByTaxId(String taxId) {
        Optional<JpaUserModel> user = jpaUserRepository.findById(taxId);
        return user.map(this::toDomain);
    }

    private JpaUserModel toJpaDomain(User user) {
        return JpaUserModel.builder()
                .name(user.getName())
                .taxId(user.getTaxId())
                .email(user.getEmail())
                .status(user.getStatus())
                .build();
    }

    private User toDomain(JpaUserModel user) {
        return User.builder()
                .name(user.getName())
                .taxId(user.getTaxId())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
