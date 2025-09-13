package br.com.joel.application.infrastructure.config;

import br.com.joel.application.infrastructure.adapters.CryptoBCryptAdapter;
import br.com.joel.application.infrastructure.adapters.TransactionAuthorizerFeignAdapter;
import br.com.joel.application.infrastructure.adapters.TransactionMQEventAdapter;
import br.com.joel.application.infrastructure.config.properties.MQProperties;
import br.com.joel.application.infrastructure.http.client.FeignTransactionAuthorizerHttpClient;
import br.com.joel.ports.CryptoPort;
import br.com.joel.ports.TransactionAuthorizerPort;
import br.com.joel.ports.TransactionEventPort;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableConfigurationProperties(MQProperties.class)
@EnableFeignClients(basePackageClasses = FeignTransactionAuthorizerHttpClient.class)
public class AdapterConfig {

    public static final String CRYPTO_BCRYPT_ADAPTER = "CryptoBCryptAdapter";
    public static final String FEIGN_AUTHORIZER_HTTP_CLIENT = "FeignAuthorizerHttpClient";
    public static final String SQS_TRANSACTION_EVENT_ADAPTER = "TransactionSQSEventAdapter";

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

    @Bean(name = SQS_TRANSACTION_EVENT_ADAPTER)
    public TransactionEventPort transactionEventPort(
            MQProperties mqProperties,
            SqsTemplate sqsTemplate
    ) {
        return new TransactionMQEventAdapter(mqProperties, sqsTemplate);
    }
}
