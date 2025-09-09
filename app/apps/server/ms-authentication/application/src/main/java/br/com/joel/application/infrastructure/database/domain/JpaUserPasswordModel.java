package br.com.joel.application.infrastructure.database.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_user_passwords")
public class JpaUserPasswordModel {

    @Id
    private String taxId;
    private String loginPassword;
    private String actionsPassword;

    @OneToOne
    @MapsId
    @JoinColumn(name = "taxId")
    private JpaUserModel user;
}
