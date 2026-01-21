package com.example.sbkafka.event;

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
}

