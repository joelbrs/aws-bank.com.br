package br.com.joel.ports.database.cache;

public interface CacheRepository {
    void save(String key, String value);
    String get(String key);
    void delete(String key);
}
