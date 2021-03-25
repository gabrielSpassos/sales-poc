package com.gabrielspassos.poc.client.kafka;

import com.gabrielspassos.poc.builder.event.SaleEventBuilder;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.config.KafkaConfig;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class PersonProducer extends KafkaReactiveProducer {

    private final KafkaConfig kafkaConfig;

    public Mono<SaleDTO> sendVoteToTopic(SaleDTO saleDTO) {
        SaleEvent saleEvent = SaleEventBuilder.build(saleDTO);
        String message = JsonUtil.getStringJson(saleEvent);
        String topic = kafkaConfig.getSalesPersonValidationRequestTopic();
        log.info("Enviando mensagem {} para topico {}", message, topic);
        sendMessages(kafkaConfig.getBootstrapServers(), topic, message);
        return Mono.just(saleDTO);
    }
}
