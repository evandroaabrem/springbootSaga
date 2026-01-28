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
public class DebitSucessListener {

    private final SagaTransferenciaService sagaService;
    private final KafkaProducer producer;

    @KafkaListener(topics = "debito-realizado")
    @Transactional
    public void onDebitoRealizado(EventoTransferencia evento) {

        boolean atualizado = sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.DEBITADA,
                StatusSaga.EM_PROCESSAMENTO,
                evento
        );

        if (!atualizado) {
            return;
        }

        // Próximo passo da SAGA
        producer.enviar("credito-comando", evento);
    }
}
