package com.example.sbkafka.dto;

import lombok.Builder;

import java.math.BigDecimal;


@Builder
public class TransferDTO {

    public Long origem;
    public Long destino;
    public BigDecimal valor;
}
