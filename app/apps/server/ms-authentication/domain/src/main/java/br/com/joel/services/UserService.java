package br.com.joel.services;

import br.com.joel.domain.domain.User;
import br.com.joel.domain.domain.enums.UserStatus;
import br.com.joel.exceptions.BusinessException;
import br.com.joel.exceptions.ExternalServiceException;
import br.com.joel.ports.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPasswordService userPasswordService;

    private final TOTPService totpService;
    private final AccountService accountService;

    public void createUser(User user) {
        try {
            user.setStatus(UserStatus.NOT_CONFIRMED);

            user.getPassword().setTaxId(user.getTaxId());

            userRepository.create(user);
            userPasswordService.create(user.getPassword());

            totpService.sendTOTP(user.getTaxId(), user.getEmail());
        } catch (BusinessException e) {
            log.error("[ERROR] Business exception when creating user: {}", e.getMessage());
            throw new BusinessException("Error creating user...", e);
        } catch (Exception e) {
            log.error("[ERROR] Exception when creating user: {}", e.getMessage());
            throw new ExternalServiceException("Error creating user...", e);
        }
    }

    public void confirmUser(String taxId, String totpCode) {
        boolean exists = userRepository.existsByTaxId(taxId);

        if (!exists) {
            log.error("[ERROR] User with id {} does not exist", taxId);
            throw new BusinessException("User with taxId " + taxId + " does not exist.");
        }

        try {
            totpService.validateTOTP(taxId, totpCode);

            userRepository.confirm(taxId);
            accountService.createAccount(taxId);
        } catch (Exception e) {
            log.error("[ERROR] Exception when confirming user: {}", e.getMessage());
            throw new ExternalServiceException("Error confirming user...", e);
        }
    }

    public User getByTaxId(String taxId) {
        return userRepository.getByTaxId(taxId)
                .orElseThrow(() -> new IllegalArgumentException("User with cpf " + taxId + " not found."));
    }
}
