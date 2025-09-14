package br.com.joel.application.infrastructure.database.domain;

import br.com.joel.domain.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_users")
public class JpaUserModel {

    @Id
    private String taxId;
    private String name;
    private String lastName;

    @Column(unique = true)
    private String email;

    @UpdateTimestamp
    private Instant updatedAt;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
