package br.com.joel.domain.domain;

import br.com.joel.domain.domain.enums.UserStatus;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String taxId;
    private String name;
    private String lastName;
    private String email;

    @Builder.Default
    private Instant updatedAt = Instant.now();
    private Instant createdAt;

    @Builder.Default
    private UserStatus status = UserStatus.NOT_CONFIRMED;

    private UserPassword password;
}
