package com.example.sbkafka.event;

import com.example.sbkafka.model.SagaTransferencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoTransferencia {
    private String transactionId;
    private Long contaOrigem;
    private Long contaDestino;
    private BigDecimal valor;
    private String status;
    private Instant timestamp;

    public static EventoTransferencia iniciar(
            Long origem,
            Long destino,
            BigDecimal valor
    ) {
        EventoTransferencia e = new EventoTransferencia();
        e.transactionId = UUID.randomUUID().toString();
        e.contaOrigem = origem;
        e.contaDestino = destino;
        e.valor = valor;
        e.status = "INICIADA";
        e.timestamp = Instant.now();
        return e;
    }

    // Converte uma entidade SagaTransferencia para EventoTransferencia
    public static EventoTransferencia fromSaga(SagaTransferencia saga) {
        if (saga == null) return null;
        Instant time = saga.getUpdatedAt() != null ? saga.getUpdatedAt() : saga.getCreatedAt();
        String status = saga.getStatus() != null ? saga.getStatus().name() : null;
        return new EventoTransferencia(
                saga.getTransactionId(),
                saga.getContaOrigem(),
                saga.getContaDestino(),
                saga.getValor(),
                status,
                time
        );
    }
}
