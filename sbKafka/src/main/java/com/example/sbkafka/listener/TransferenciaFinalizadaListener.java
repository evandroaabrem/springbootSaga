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
public class TransferenciaFinalizadaListener {
    private final SagaTransferenciaService sagaService;
    private final KafkaProducer producer;


    @KafkaListener(topics = "credito-realizado")
    @Transactional
    public void concluir(EventoTransferencia evento) {

        boolean ok = sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.CREDITADA,
                StatusSaga.CONCLUIDA,
                evento
        );

        if (!ok) return;

        producer.enviar("transferencia-concluida", evento);
    }
}
