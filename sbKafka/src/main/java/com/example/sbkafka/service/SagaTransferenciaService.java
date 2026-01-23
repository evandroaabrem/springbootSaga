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

    private final SagaEventoService sagaEventoService;



    @Transactional
    public void createSaga(EventoTransferencia evento) {

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
    public boolean atualizarStatusSeEsperado(
            String transactionId,
            StatusSaga statusEsperado,
            StatusSaga novoStatus,
            Object evento) {

        SagaTransferencia saga = repository.findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new IllegalStateException("Saga não encontrada: " + transactionId)
                );

        StatusSaga statusAtual = saga.getStatus();

        // 🔁 Idempotência / fora de ordem
        if (statusAtual != statusEsperado) {

            sagaEventoService.registrar(
                    transactionId,
                    "TRANSICAO_IGNORADA",
                    statusAtual,
                    novoStatus,
                    evento
            );

            return false;
        }

        saga.setStatus(novoStatus);
        repository.save(saga);
        // 🧾 Auditoria oficial da transição
        sagaEventoService.registrar(
                transactionId,
                "STATUS_ATUALIZADO",
                statusEsperado,
                novoStatus,
                evento
        );
        return true;
    }

    /**
     * Busca uma Saga pelo TransactionId
     */
    @Transactional(readOnly = true)
    public SagaTransferencia buscarPorTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalStateException("Saga não encontrada: " + transactionId));
    }

    @Transactional
    public void atualizarStatusComErro(
            String transactionId,
            StatusSaga statusOrigem,
            StatusSaga statusDestino,
            Object evento,
            Exception erro
    ) {
        sagaEventoService.registrarErro(
                transactionId,
                "ERRO_PROCESSAMENTO",
                statusOrigem,
                statusDestino,
                evento,
                erro
        );
    }

}
