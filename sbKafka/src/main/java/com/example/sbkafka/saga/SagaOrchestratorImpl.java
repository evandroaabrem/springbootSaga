package com.example.sbkafka.saga;

import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.kafka.KafkaProducer;
import com.example.sbkafka.model.StatusSaga;
import com.example.sbkafka.service.SagaTransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaOrchestratorImpl implements SagaOrchestrator {

    private final SagaTransferenciaService sagaService;
    private final KafkaProducer producer;

    @Override
    public void iniciarTransferencia(EventoTransferencia evento) {

        /*boolean ok = sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.INICIADA,
                StatusSaga.EM_PROCESSAMENTO
        );

        if (!ok) return;

        producer.enviar("debito-comando", evento);*/

        sagaService.createSaga(evento);

        producer.enviar("debito-comando", evento);

        ////producer.enviar("transferencia-iniciada", evento);
    }

    @Override
    public void tratarDebitoRealizado(EventoTransferencia evento) {

        boolean ok = sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.DEBITADA,
                StatusSaga.EM_PROCESSAMENTO, evento
        );

        if (!ok) return;

        producer.enviar("credito-comando", evento);
    }

    @Override
    public void tratarCreditoRealizado(EventoTransferencia evento) {

        boolean ok = sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.CREDITADA,
                StatusSaga.FINALIZADA, evento
        );

        if (!ok) return;

        producer.enviar("transferencia-finalizada", evento);

    }

    @Override
    public void tratarFalha(EventoTransferencia evento, String origem) {
        boolean ok = sagaService.atualizarStatusSeEsperado(
                evento.getTransactionId(),
                StatusSaga.EM_PROCESSAMENTO,
                StatusSaga.FALHOU, evento
        );

        if (!ok) return;

        producer.enviar("estorno-comando", evento);
    }
}
