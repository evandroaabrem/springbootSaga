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
public class DebitCommandDltListener {

    private final SagaTransferenciaService sagaService;
    private final KafkaProducer producer;

    @KafkaListener(
            topics = "debito-comando-DLT",
            groupId = "sbkafka-group-dlt",
            containerFactory = "dltKafkaListenerContainerFactory"
    )
    @Transactional
    public void tratarFalhaDebito(EventoTransferencia evento) {

        sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.INICIADA,
                StatusSaga.FALHOU, evento
        );

        // Dispara compensação
        producer.enviar("estorno-comando", evento);

    }

}
