package br.com.joel.application.infrastructure.config;

import br.com.joel.application.infrastructure.adapters.CryptoBCryptAdapter;
import br.com.joel.application.infrastructure.adapters.TransactionAuthorizerFeignAdapter;
import br.com.joel.application.infrastructure.http.client.FeignTransactionAuthorizerHttpClient;
import br.com.joel.ports.CryptoPort;
import br.com.joel.ports.TransactionAuthorizerPort;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableFeignClients(basePackageClasses = FeignTransactionAuthorizerHttpClient.class)
public class AdapterConfig {

    public static final String CRYPTO_BCRYPT_ADAPTER = "CryptoBCryptAdapter";
    public static final String FEIGN_AUTHORIZER_HTTP_CLIENT = "FeignAuthorizerHttpClient";

    @Bean(name = CRYPTO_BCRYPT_ADAPTER)
    public CryptoPort cryptoPort(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new CryptoBCryptAdapter(bCryptPasswordEncoder);
    }

    @Bean(name = FEIGN_AUTHORIZER_HTTP_CLIENT)
    public TransactionAuthorizerPort transactionAuthorizer(
            FeignTransactionAuthorizerHttpClient feignTransactionAuthorizerHttpClient
    ) {
        return new TransactionAuthorizerFeignAdapter(feignTransactionAuthorizerHttpClient);
    }
}
