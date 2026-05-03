package br.com.awsbank.app.authentication.ports.out;

import br.com.awsbank.app.authentication.domain.models.Token;

public interface TokenPort {
    Token getToken();
}
