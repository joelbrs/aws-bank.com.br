package br.com.joel.application.infrastructure.adapters;

import br.com.joel.application.infrastructure.http.client.FeignTransactionAuthorizerHttpClient;
import br.com.joel.ports.TransactionAuthorizerPort;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionAuthorizerFeignAdapter implements TransactionAuthorizerPort {

    private final FeignTransactionAuthorizerHttpClient feignTransactionAuthorizerHttpClient;

    @Override
    public void authorize() {
        try {
            feignTransactionAuthorizerHttpClient.authorize();
        } catch (FeignException e) {
            //TODO: add personalized exception
            throw new RuntimeException(e);
        }
    }
}
