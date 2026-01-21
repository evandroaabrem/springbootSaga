package com.example.sbkafka.saga;


import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferenciaSagaOrchestrator {

    private final KafkaProducer producer;

    public void iniciar(EventoTransferencia evento) {
        producer.enviar("transferencia-iniciada", evento);
    }

    @KafkaListener(topics = "debito-realizado")
    public void onDebito(EventoTransferencia evento) {
        producer.enviar("credito-comando", evento);
    }

    @KafkaListener(topics = "credito-realizado")
    public void onCredito(EventoTransferencia evento) {
        producer.enviar("transferencia-finalizada", evento);
    }

    @KafkaListener(topics = "transferencia-falhou")
    public void onFalha(EventoTransferencia evento) {
        producer.enviar("estorno-comando", evento);
    }
}
