package com.example.sbkafka.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "saga_transferencia",
        schema = "saga",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_transaction_id",
                columnNames = "transaction_id"
        )
)
@NoArgsConstructor
@Setter
public class SagaTransferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false, updatable = false)
    private String transactionId;

    @Column(name = "conta_origem", nullable = false)
    private Long contaOrigem;

    @Column(name = "conta_destino", nullable = false)
    private Long contaDestino;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSaga status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
        this.status = StatusSaga.INICIADA;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    // getters e setters
}
