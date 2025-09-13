package br.com.joel.application.infrastructure.adapters;

import br.com.joel.application.infrastructure.config.properties.MQProperties;
import br.com.joel.domain.domain.Transaction;
import br.com.joel.ports.TransactionEventPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionMQEventAdapter implements TransactionEventPort {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final MQProperties mqProperties;
    private final SqsTemplate sqsTemplate;

    @Override
    public void publish(Transaction transaction) {
        try {
            sqsTemplate.send(mqProperties.getQueues().get(MQProperties.TRANSACTION_QUEUE).getName(), OBJECT_MAPPER.writeValueAsString(transaction));
        } catch (Exception e) {
            //TODO: add personalized exception
            throw new RuntimeException("Failed to publish transaction event to SQS", e);
        }
    }
}
