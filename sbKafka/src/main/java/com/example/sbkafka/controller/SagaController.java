package com.example.sbkafka.controller;

import com.example.sbkafka.dto.TransferDTO;
import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.model.SagaTransferencia;
import com.example.sbkafka.saga.SagaOrchestrator;
import com.example.sbkafka.saga.TransferenciaSagaOrchestrator;
import com.example.sbkafka.service.SagaReprocessamentoService;
import com.example.sbkafka.service.SagaTransferenciaService;
import com.example.sbkafka.kafka.KafkaProducer;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/saga")
@AllArgsConstructor
public class SagaController {

    private final SagaOrchestrator orchestrator;

    private final SagaTransferenciaService sagaService;

    private final KafkaProducer producer;

    private final SagaReprocessamentoService service;

    /**
     * Reprocessa o último evento da SAGA
     */
    @PostMapping("/{transactionId}/ultimo")
    public ResponseEntity<Void> reprocessarUltimo(@PathVariable String transactionId) {
        service.reprocessarUltimoEvento(transactionId);
        return ResponseEntity.accepted().build();
    }

    /**
     * Reprocessa um evento específico
     */
    @PostMapping("/evento/{eventoId}")
    public ResponseEntity<Void> reprocessarEvento(@PathVariable Long eventoId) {
        service.reprocessarEvento(eventoId);
        return ResponseEntity.accepted().build();
    }


    @PostMapping("/transferencias")
    public ResponseEntity<?> transferir() {
        EventoTransferencia evento =
                EventoTransferencia.iniciar(1L, 2L, BigDecimal.valueOf(100));
        /////saga.iniciar(evento);
        orchestrator.iniciarTransferencia(evento);

        return ResponseEntity.ok().build();
    }

    // Endpoint para simular transferência PIX via SAGA (origem, destino e valor no body)
    @PostMapping("/pix")
    public ResponseEntity<String> transferirPix(@RequestBody TransferDTO req) {
        EventoTransferencia evento = EventoTransferencia.iniciar(
                req.origem,
                req.destino,
                req.valor
        );

        orchestrator.iniciarTransferencia(evento);
        // Retorna o transactionId para facilitar reprocessamentos/tests
        return ResponseEntity.accepted().body(evento.getTransactionId());
    }

    @PostMapping("/{transactionId}/reprocessar")
    public ResponseEntity<Void> reprocessar(@PathVariable String transactionId) {

        SagaTransferencia saga = sagaService.buscarPorTransactionId(transactionId);

        EventoTransferencia evento = EventoTransferencia.fromSaga(saga);

        producer.enviar("debito-comando", evento);

        return ResponseEntity.accepted().build();
    }


}

