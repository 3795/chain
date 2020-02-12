package com.cdqd.component;

import com.cdqd.data.MetaData;
import com.cdqd.service.BlockChainService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Kafka消费者
 * Created At 2020/2/11
 */
@Component
public class KafkaConsumer {

    @Autowired
    private BlockChainService blockChainService;

    private List<String> messageList = new ArrayList<>();

    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * 批量消费Kafka中的消息，并且手动提交offset
     *
     * @param consumerRecords
     * @param ack
     */
    @KafkaListener(topics = "block-chain", containerFactory = "kafkaListenerContainerFactory")
    public void listener(List<ConsumerRecord> consumerRecords, Acknowledgment ack) {
        messageList.clear(); // 清空list
        for (ConsumerRecord cr : consumerRecords) {
            messageList.add(cr.value().toString());
        }
        if (blockChainService.packAndBroadcast(messageList)) {
            ack.acknowledge();      // 手动提交offset
        }

    }
}
