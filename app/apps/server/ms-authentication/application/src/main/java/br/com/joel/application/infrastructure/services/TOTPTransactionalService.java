package br.com.joel.application.infrastructure.services;

import br.com.joel.ports.EmailPort;
import br.com.joel.ports.database.cache.TOTPCacheRepository;
import br.com.joel.services.TOTPService;
import org.springframework.transaction.annotation.Transactional;

public class TOTPTransactionalService extends TOTPService {
    public TOTPTransactionalService(EmailPort emailPort, TOTPCacheRepository totpCacheRepository) {
        super(emailPort, totpCacheRepository);
    }

    @Transactional
    @Override
    public void sendTOTP(String taxId, String to) {
        super.sendTOTP(taxId, to);
    }
}
