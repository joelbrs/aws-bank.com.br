package br.com.joel.ports.database.cache;

public interface CacheRepository {
    void save(String taxId, String code);
    String get(String taxId);
    void delete(String taxId);
}
