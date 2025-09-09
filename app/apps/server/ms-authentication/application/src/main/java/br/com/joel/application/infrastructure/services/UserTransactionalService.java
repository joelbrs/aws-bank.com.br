package br.com.joel.application.infrastructure.services;

import br.com.joel.domain.domain.User;
import br.com.joel.ports.database.UserRepository;
import br.com.joel.services.*;
import org.springframework.transaction.annotation.Transactional;

public class UserTransactionalService extends UserService {
    public UserTransactionalService(
            UserRepository userRepository,
            UserPasswordService userPasswordService,
            TOTPService totpService,
            AccountService accountService
    ) {
        super(userRepository, userPasswordService, totpService, accountService);
    }

    @Transactional
    @Override
    public void createUser(User user) {
        super.createUser(user);
    }

    @Transactional
    @Override
    public void confirmUser(String taxId, String totpCode){
        super.confirmUser(taxId, totpCode);
    }
}
