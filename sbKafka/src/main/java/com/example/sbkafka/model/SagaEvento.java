package com.example.sbkafka.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "saga_evento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SagaEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(name = "status_origem")
    private String statusOrigem;

    @Column(name = "status_destino")
    private String statusDestino;

    @Column(name = "payload", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    @Column
    private String erro;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
