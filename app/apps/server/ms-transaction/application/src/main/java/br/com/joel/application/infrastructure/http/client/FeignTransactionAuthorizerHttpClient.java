package br.com.joel.application.infrastructure.http.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "authorizer",
        url = "${http.client.authorizer.url}",
        path = "${http.client.authorizer.path}"
)
public interface FeignTransactionAuthorizerHttpClient {

    @PostMapping("/authorize")
    void authorize();
}
