package br.com.joel.services;

import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.enums.UserStatus;
import br.com.joel.ports.database.UserPasswordRepository;
import br.com.joel.ports.database.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPasswordRepository userPasswordRepository;

    private final TOTPService totpService;
    private final CryptoService cryptoService;
    private final AccountService accountService;

    private final String jwtSecret;

    //TODO: @Transactional
    //TODO: create user's passwords service to handle password logic (including validation and cryptography)
    public void createUser(User user) {
        try {
            user.setTaxId(cryptoService.encrypt(user.getTaxId(), jwtSecret));
            user.setStatus(UserStatus.NOT_CONFIRMED);

            user.getPassword().setTaxId(user.getTaxId());

            user.getPassword()
                    .setLoginPassword(cryptoService.hash(user.getPassword().getLoginPassword()));
            user.getPassword()
                    .setActionsPassword(cryptoService.hash(user.getPassword().getActionsPassword()));

            userRepository.create(user);
            userPasswordRepository.create(user.getPassword());

            totpService.sendTOTP(user.getTaxId(), user.getEmail());
        } catch (Exception e) {
            //TODO: add logging
            //TODO: create personalized exception
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

    //TODO: @Transactional
    public void confirmUser(String taxId, String totpCode) {
        boolean exists = userRepository.existsByTaxId(taxId);

        if (!exists) {
            //TODO: add logging
            throw new IllegalArgumentException("User with taxId " + taxId + " does not exist.");
        }

        try {
            totpService.validateTOTP(taxId, totpCode);

            userRepository.confirm(taxId);
            accountService.createAccount(taxId);
        } catch (Exception e) {
            //TODO: add logging
            //TODO: create personalized exception
        }
    }
}
