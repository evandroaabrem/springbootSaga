package com.example.sbkafka.kafka;

import com.example.sbkafka.event.EventoTransferencia;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    @Qualifier("kafkaTemplateEvento")
    private final KafkaTemplate<String, EventoTransferencia> kafkaTemplate;

    public void enviar(String topic, EventoTransferencia evento) {
        kafkaTemplate.send(topic, evento.getTransactionId(), evento);
    }
}
