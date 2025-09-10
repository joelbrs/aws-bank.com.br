package br.com.joel.application.infrastructure.services;

import br.com.joel.ports.EmailPort;
import br.com.joel.ports.database.cache.CacheRepository;
import br.com.joel.services.TOTPService;
import org.springframework.transaction.annotation.Transactional;

public class TOTPTransactionalService extends TOTPService {
    public TOTPTransactionalService(EmailPort emailPort, CacheRepository cacheRepository) {
        super(emailPort, cacheRepository);
    }

    @Transactional
    @Override
    public void sendTOTP(String taxId, String to) {
        super.sendTOTP(taxId, to);
    }
}
