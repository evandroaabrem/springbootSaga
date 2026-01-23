package com.example.sbkafka.service;

import com.example.sbkafka.model.SagaEvento;
import com.example.sbkafka.model.StatusSaga;
import com.example.sbkafka.repository.SagaEventoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class SagaEventoService {

    private final SagaEventoRepository repository;
    private final ObjectMapper objectMapper;

    public void registrar(
            String transactionId,
            String tipoEvento,
            StatusSaga statusOrigem,
            StatusSaga statusDestino,
            Object payload
    ) {
        salvar(transactionId, tipoEvento, statusOrigem, statusDestino, payload, null);
    }

    public void registrarErro(
            String transactionId,
            String tipoEvento,
            StatusSaga statusOrigem,
            StatusSaga statusDestino,
            Object payload,
            Exception erro
    ) {
        salvar(
                transactionId,
                tipoEvento,
                statusOrigem,
                statusDestino,
                payload,
                erro.getMessage()
        );
    }

    private void salvar(
            String transactionId,
            String tipoEvento,
            StatusSaga statusOrigem,
            StatusSaga statusDestino,
            Object payload,
            String erro
    ) {
        try {
            SagaEvento evento = SagaEvento.builder()
                    .transactionId(transactionId)
                    .tipoEvento(tipoEvento)
                    .statusOrigem(statusOrigem != null ? statusOrigem.name() : null)
                    .statusDestino(statusDestino != null ? statusDestino.name() : null)
                    .payload(objectMapper.writeValueAsString(payload))
                    .erro(erro)
                    .createdAt(OffsetDateTime.now())
                    .build();

            repository.save(evento);

        } catch (Exception e) {
            // 🔥 auditoria NUNCA pode quebrar a SAGA
            System.err.println("Erro ao registrar auditoria da saga: " + e.getMessage());
        }
    }
}

