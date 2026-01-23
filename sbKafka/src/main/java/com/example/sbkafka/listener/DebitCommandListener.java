package com.example.sbkafka.listener;

import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.kafka.KafkaProducer;
import com.example.sbkafka.model.StatusSaga;
import com.example.sbkafka.service.SagaTransferenciaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebitCommandListener {
    private final SagaTransferenciaService sagaService;
    private final KafkaProducer producer;

    @KafkaListener(topics = "debito-comando")
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(
                    delay = 2000,
                    multiplier = 2.0
            ),
            dltTopicSuffix = "-DLT"
    )
    @Transactional
    public void debitar(EventoTransferencia evento) {

        boolean atualizado = sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.INICIADA,
                StatusSaga.DEBITADA, evento
        );

        if (!atualizado) {
            return;
        }

        // 💥 Simule falha para teste (remova depois)
        // if (true) throw new RuntimeException("Falha simulada");

        ///throw new RuntimeException("Falha simulada");

        producer.enviar("debito-realizado", evento);
    }
}
