package net.vino9.vino.demo.biz.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private String topic = "process-in";

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        log.info("EventPublisher initialized with topic: {}", topic);
    }

    public void publishEvent(String refId) {
        rabbitTemplate.convertAndSend(topic, "", refId);
        log.info("Published event to RabbitMQ topic {}: {}", topic, refId);
    }
}
