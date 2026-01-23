package com.example.sbkafka.service;

import com.example.sbkafka.event.EventoTransferencia;
import com.example.sbkafka.kafka.KafkaProducer;
import com.example.sbkafka.model.SagaEvento;
import com.example.sbkafka.model.SagaTransferencia;
import com.example.sbkafka.model.StatusSaga;
import com.example.sbkafka.repository.SagaEventoRepository;
import com.example.sbkafka.repository.SagaTransferenciaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SagaReprocessamentoService {

    private final SagaEventoRepository eventoRepository;
    private final KafkaProducer producer;
    private final ObjectMapper objectMapper;
    private final SagaTransferenciaRepository sagaTransferenciaRepository;

    @Transactional(readOnly = true)
    public void reprocessarUltimoEvento(String transactionId) {

        SagaTransferencia saga = sagaTransferenciaRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new IllegalStateException("SAGA não encontrada: " + transactionId)
                );

        // 🚫 Proteção: SAGA finalizada
        if (saga.getStatus() == StatusSaga.CONCLUIDA) {
            throw new IllegalStateException(
                    "SAGA já concluída. Reprocessamento não permitido."
            );
        }

        SagaEvento evento = eventoRepository
                .findTopByTransactionIdAndTipoEventoInOrderByCreatedAtDesc(
                        transactionId,
                        List.of(
                                "DEBITO_COMANDO",
                                "CREDITO_COMANDO",
                                "TRANSFERENCIA_INICIADA"
                        )
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Nenhum evento reprocessável encontrado para a transação " + transactionId
                        )
                );

        republicar(evento);
    }

    @Transactional(readOnly = true)
    public void reprocessarEvento(Long eventoId) {

        SagaEvento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() ->
                        new IllegalStateException("Evento não encontrado: " + eventoId)
                );

        republicar(evento);
    }

    private void republicar(SagaEvento evento) {

        String topic = mapearTopico(evento.getStatusDestino());

        EventoTransferencia payload =
                converterPayload(evento.getPayload());

        producer.enviar(topic, payload);
    }

    private EventoTransferencia converterPayload(String payloadJson) {
        try {
            return objectMapper.readValue(payloadJson, EventoTransferencia.class);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Erro ao converter payload da SAGA para EventoTransferencia", e
            );
        }
    }

    private String mapearTopico(String tipoEvento) {
        return switch (tipoEvento) {
            case "FALHOU" -> "debito-comando";
            case "DEBITO_COMANDO" -> "debito-comando";
            case "CREDITO_COMANDO" -> "credito-comando";
            case "TRANSFERENCIA_INICIADA" -> "transferencia-iniciada";
            default -> throw new IllegalArgumentException(
                    "Tipo de evento não suportado para reprocessamento: " + tipoEvento
            );
        };
    }
}

