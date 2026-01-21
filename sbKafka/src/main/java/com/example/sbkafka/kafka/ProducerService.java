package com.example.sbkafka.kafka;

import com.example.sbkafka.dto.PedidoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    private final KafkaTemplate<String, PedidoDTO> kafkaTemplate;

    @Value("${app.kafka.topic:pedidos-topic}")
    private String topic;

    public ProducerService(KafkaTemplate<String, PedidoDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPedido(PedidoDTO pedido) {
        logger.info("Enviando pedido para o tópico {}: {}", topic, pedido);
        kafkaTemplate.send(topic, String.valueOf(pedido.getId()), pedido);
    }
}
