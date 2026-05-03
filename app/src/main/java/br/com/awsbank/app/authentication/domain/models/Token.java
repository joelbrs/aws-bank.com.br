package br.com.awsbank.app.authentication.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    private String access_token;
    private Long expires_in;
    private String token_type;
    private Long refresh_expires_in;
    private String scope;
}
