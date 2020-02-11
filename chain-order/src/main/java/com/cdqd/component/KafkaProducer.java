package com.cdqd.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Description: Kafka生产者
 * Created At 2020/2/11
 */
@Component
public class KafkaProducer {

    private final static String topic = "block-chain";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        kafkaTemplate.send(topic, message);
    }


}
