package br.com.joel.application.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "async.queue")
public class MQProperties {

    public static final String TRANSACTION_QUEUE = "transaction";
    public static final String TRANSACTION_EXTRACT_QUEUE = "extract";

    private Map<String, QueueProperties> queues;

    @Data
    public static class QueueProperties {
        private String name;
    }
}
