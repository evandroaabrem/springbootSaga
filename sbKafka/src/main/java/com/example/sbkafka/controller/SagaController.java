package com.example.sbkafka.controller;

import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.saga.TransferenciaSagaOrchestrator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/saga")
@AllArgsConstructor
public class SagaController {

    private final TransferenciaSagaOrchestrator saga;

    @PostMapping("/transferencias")
    public ResponseEntity<?> transferir() {
        EventoTransferencia evento =
                EventoTransferencia.iniciar(1L, 2L, BigDecimal.valueOf(100));
        saga.iniciar(evento);
        return ResponseEntity.ok().build();
    }}

