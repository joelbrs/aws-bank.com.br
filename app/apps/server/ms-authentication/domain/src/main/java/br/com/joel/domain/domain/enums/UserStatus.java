package br.com.joel.domain.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE(1L, "Active"),
    INACTIVE(2L, "Inactive"),
    NOT_CONFIRMED(3L, "Not Confirmed");

    private final Long identifier;
    private final String description;
}