package br.com.joel.services;

import br.com.joel.domain.domain.UserPassword;
import br.com.joel.exceptions.BusinessException;
import br.com.joel.ports.database.UserPasswordRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserPasswordService {

    private final UserPasswordRepository userPasswordRepository;
    private final CryptoService cryptoService;

    public void create(UserPassword userPassword) {
        this.validate(userPassword);

        userPassword.setLoginPassword(cryptoService.hash(userPassword.getLoginPassword()));
        userPassword.setActionsPassword(cryptoService.hash(userPassword.getActionsPassword()));

        userPasswordRepository.create(userPassword);
    }

    private void validate(UserPassword userPassword) {
        if (userPassword.getActionsPassword().length() != 6) {
            throw new BusinessException("Invalid actions password");
        }

        if (userPassword.getLoginPassword().length() != 8) {
            throw new BusinessException("Invalid login password");
        }
    }
}

