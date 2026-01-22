package com.example.sbkafka.repository;

import com.example.sbkafka.model.SagaTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SagaTransferenciaRepository
        extends JpaRepository<SagaTransferencia, Long> {

    Optional<SagaTransferencia> findByTransactionId(String transactionId);

    boolean existsByTransactionId(String transactionId);
}
