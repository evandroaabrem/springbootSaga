package com.example.sbkafka.producer;


import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("debitoListenerKafka")
@RequiredArgsConstructor
public class DebitPublisher {

   private final KafkaProducer producer;

   //// @KafkaListener(topics = "debito-comando")
    public void debitar(EventoTransferencia evento) {
        try {
            // simula débito
            producer.enviar("debito-realizado", evento);
        } catch (Exception e) {
            producer.enviar("transferencia-falhou", evento);
        }
    }
}
