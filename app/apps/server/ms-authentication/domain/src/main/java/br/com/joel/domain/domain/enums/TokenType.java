package br.com.joel.domain.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    ACCESS_TOKEN("BANK.COM.BR.ACCESS.TOKEN", 60 * 15, "access"),
    REFRESH_TOKEN("BANK.COM.BR.REFRESH.TOKEN", 60 * 60 * 24 * 7, "refresh");

    private final String cookieName;
    private final Integer expiration;
    private final String claimName;
}
