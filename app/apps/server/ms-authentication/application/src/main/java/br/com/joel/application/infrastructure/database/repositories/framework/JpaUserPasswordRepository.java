package br.com.joel.application.infrastructure.database.repositories.framework;

import br.com.joel.application.infrastructure.database.domain.JpaUserPasswordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserPasswordRepository extends JpaRepository<JpaUserPasswordModel, Long> {
}
