package com.example.sbkafka.listener;

import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.kafka.KafkaProducer;
import com.example.sbkafka.model.StatusSaga;
import com.example.sbkafka.service.SagaTransferenciaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditoCommandListener {

    private final SagaTransferenciaService sagaService;
    private final KafkaProducer producer;

    @KafkaListener(topics = "credito-comando")
    @Transactional
    public void creditar(EventoTransferencia evento) {

        boolean ok = sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.DEBITADA,
                StatusSaga.CREDITADA,
                evento
        );
        System.out.println("ok "+ ok);

        if (!ok) return;

        // simula crédito OK
        producer.enviar("credito-realizado", evento);
    }
}
