package com.example.sbkafka.kafka;


import com.example.sbkafka.event.EventoTransferencia;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebitoListener {

   private final KafkaProducer producer;

    @KafkaListener(topics = "debito-comando")
    public void debitar(EventoTransferencia evento) {
        try {
            // simula débito
            producer.enviar("debito-realizado", evento);
        } catch (Exception e) {
            producer.enviar("transferencia-falhou", evento);
        }
    }
}
