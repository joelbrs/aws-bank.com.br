package br.com.joel.domain.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPassword {
    private String taxId;
    private String loginPassword;
    private String actionsPassword;
}
