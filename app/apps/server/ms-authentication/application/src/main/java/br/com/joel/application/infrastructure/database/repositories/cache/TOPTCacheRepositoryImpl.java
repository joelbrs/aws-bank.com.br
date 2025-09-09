package br.com.joel.application.infrastructure.database.repositories.cache;

import br.com.joel.ports.database.cache.TOTPCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class TOPTCacheRepositoryImpl implements TOTPCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String taxId, String code) {
        redisTemplate.opsForValue().set(taxId, code);
    }

    @Override
    public String get(String taxId) {
        return redisTemplate.opsForValue().get(taxId);
    }

    @Override
    public void delete(String taxId) {
        redisTemplate.delete(taxId);
    }
}
