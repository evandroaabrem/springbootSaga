package com.example.sbkafka.saga;

import com.example.sbkafka.event.EventoTransferencia;

public interface SagaOrchestrator {
    void iniciarTransferencia(EventoTransferencia evento);

    void tratarDebitoRealizado(EventoTransferencia evento);

    void tratarCreditoRealizado(EventoTransferencia evento);

    void tratarFalha(EventoTransferencia evento, String origem);
}
