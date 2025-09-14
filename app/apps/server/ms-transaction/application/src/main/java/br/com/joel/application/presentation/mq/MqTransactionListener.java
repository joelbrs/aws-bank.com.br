package br.com.joel.application.presentation.mq;

import br.com.joel.application.infrastructure.config.properties.MQProperties;
import br.com.joel.domain.domain.Transaction;
import br.com.joel.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MqTransactionListener {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final MQProperties mqProperties;
    private final TransactionService transactionService;

    @SqsListener("#{mqProperties.queues['transaction'].name}")
    public void listen(String payload) {
        transactionService.processTransactionEvent(OBJECT_MAPPER.convertValue(payload, Transaction.class));
    }
}
