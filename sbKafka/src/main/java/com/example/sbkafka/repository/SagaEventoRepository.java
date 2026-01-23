package com.example.sbkafka.repository;

import com.example.sbkafka.model.SagaEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SagaEventoRepository
        extends JpaRepository<SagaEvento, Long> {

    Optional<SagaEvento> findTopByTransactionIdAndTipoEventoInOrderByCreatedAtDesc(
            String transactionId,
            List<String> tipos
    );


    Optional<SagaEvento> findTopByTransactionIdOrderByCreatedAtDesc(String transactionId);

}

