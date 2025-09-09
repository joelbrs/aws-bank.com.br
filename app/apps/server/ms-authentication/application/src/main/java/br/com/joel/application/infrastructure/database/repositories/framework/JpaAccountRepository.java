package br.com.joel.application.infrastructure.database.repositories.framework;

import br.com.joel.application.infrastructure.database.domain.JpaAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountRepository extends JpaRepository<JpaAccountModel, Long> {
}
