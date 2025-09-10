package br.com.joel.application.infrastructure.database.repositories.framework;

import br.com.joel.application.infrastructure.database.domain.JpaUserPasswordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserPasswordRepository extends JpaRepository<JpaUserPasswordModel, Long> {

    @Query("select u from JpaUserPasswordModel u where u.taxId = :taxId and u.user.status = br.com.joel.domain.domain.enums.UserStatus.ACTIVE")
    Optional<JpaUserPasswordModel> getByTaxIdIfUserIsActive(String taxId);
}
