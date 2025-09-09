package br.com.joel.services;

import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.enums.UserStatus;
import br.com.joel.ports.database.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPasswordService userPasswordService;

    private final TOTPService totpService;
    private final CryptoService cryptoService;
    private final AccountService accountService;

    private final String jwtSecret;

    public void createUser(User user) {
        try {
            user.setTaxId(cryptoService.encrypt(user.getTaxId(), jwtSecret));
            user.setStatus(UserStatus.NOT_CONFIRMED);

            user.getPassword().setTaxId(user.getTaxId());

            userRepository.create(user);
            userPasswordService.create(user.getPassword());

            totpService.sendTOTP(user.getTaxId(), user.getEmail());
        } catch (Exception e) {
            //TODO: add logging
            //TODO: create personalized exception
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

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
