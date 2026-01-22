package com.example.sbkafka.service;


import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.model.SagaTransferencia;
import com.example.sbkafka.model.StatusSaga;
import com.example.sbkafka.repository.SagaTransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SagaTransferenciaService {

    private final SagaTransferenciaRepository repository;

    /**
     * Início da SAGA
     */
    @Transactional
    public void iniciar(EventoTransferencia evento) {

        if (repository.existsByTransactionId(evento.getTransactionId())) {
            // 🔁 Idempotência
            return;
        }

        SagaTransferencia saga = new SagaTransferencia();
        saga.setTransactionId(evento.getTransactionId());
        saga.setContaOrigem(evento.getContaOrigem());
        saga.setContaDestino(evento.getContaDestino());
        saga.setValor(evento.getValor());
        saga.setStatus(StatusSaga.INICIADA);

        repository.save(saga);
    }

    /**
     * Atualização de status
     */
    @Transactional
    public void atualizarStatus(String transactionId, StatusSaga status) {

        SagaTransferencia saga = repository.findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new IllegalStateException("Saga não encontrada: " + transactionId)
                );

        saga.setStatus(status);
        repository.save(saga);
    }
}

