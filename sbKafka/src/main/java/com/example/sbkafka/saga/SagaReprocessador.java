package com.example.sbkafka.saga;

import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.model.SagaTransferencia;
import lombok.RequiredArgsConstructor;
import com.example.sbkafka.kafka.KafkaProducer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaReprocessador {

    private final KafkaProducer producer;

    public void reprocessar(SagaTransferencia saga) {

        EventoTransferencia evento = EventoTransferencia.fromSaga(saga);

        switch (saga.getStatus()) {

            case FALHOU -> {
                // Decide se reprocessa débito ou crédito
                producer.enviar("debito-comando", evento);
            }

            case DEBITADA -> {
                producer.enviar("credito-comando", evento);
            }

            case COMPENSANDO -> {
                producer.enviar("estorno-comando", evento);
            }

            default -> throw new IllegalStateException(
                    "Saga em estado não reprocessável: " + saga.getStatus()
            );
        }
    }
}
