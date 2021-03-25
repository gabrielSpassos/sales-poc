package com.gabrielspassos.poc.client.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.service.PersonValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PersonConsumer {

    private final PersonValidationService personValidationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.sales-person-validation-request-topic}", groupId = "${kafka.consumer-group}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String event, Acknowledgment ack,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition, @Header(KafkaHeaders.OFFSET) String offset) {
        try {
            log.info("Partição {}, Offset {}, Mensagem: {}", partition, offset, event);
            SaleEvent saleEvent = convertValue(event);
            personValidationService.createPersonValidation(saleEvent)
                    .doOnSuccess(entity -> log.info("Validação da pessoa salva {}", entity))
                    .doFinally(signalType -> ack.acknowledge())
                    .subscribe();
        } catch (Exception e) {
            log.error(String.format("Erro ao processar a mensagem de validação da pessoa %s", event), e);
            throw e;
        }
    }

    private SaleEvent convertValue(String event) {
        try {
            return objectMapper.readValue(event, SaleEvent.class);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

}
