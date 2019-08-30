package com.baidu.springbootstudy.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerApp {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerApp.class);

    @KafkaListener(topics = "labelEngineWebLog_from_machineB_test001")
    public void getMessage (ConsumerRecord<?, ?> record) {
        logger.info("topic = " + record.topic() + ", offset = " + record.offset() + ", value = " + record.value());
    }
}
