package br.com.joel.application.infrastructure.adapters;

import br.com.joel.application.infrastructure.config.properties.MQProperties;
import br.com.joel.domain.domain.Transaction;
import br.com.joel.domain.domain.TransactionExtractPayload;
import br.com.joel.exceptions.ExternalServiceException;
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
        this.send(MQProperties.TRANSACTION_QUEUE, transaction);
    }

    @Override
    public void publishTransactionExtractRequest(TransactionExtractPayload transactionExtractPayload) {
        this.send(MQProperties.TRANSACTION_EXTRACT_QUEUE, transactionExtractPayload);
    }

    private <T> void send(String queueName, T payload) {
        try {
            sqsTemplate.send(mqProperties.getQueues().get(queueName).getName(), OBJECT_MAPPER.writeValueAsString(payload));
        } catch (Exception e) {
            throw new ExternalServiceException("Failed to publish event to SQS", e);
        }
    }
}
