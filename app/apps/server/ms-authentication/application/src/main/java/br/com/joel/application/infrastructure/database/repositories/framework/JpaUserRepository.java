package br.com.joel.application.infrastructure.database.repositories.framework;

import br.com.joel.application.infrastructure.database.domain.JpaUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserModel, String> {

    @Modifying
    @Query("update JpaUserModel u set u.status = br.com.joel.domain.domain.enums.UserStatus.ACTIVE where u.taxId = :taxId")
    void confirm(String taxId);
}
