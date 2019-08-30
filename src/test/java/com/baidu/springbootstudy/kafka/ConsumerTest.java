package com.baidu.springbootstudy.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerTest {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerTest.class);

    Properties props = new Properties();
    KafkaConsumer<String, String> kafkaConsumer = null;

    @Before
    public void before () {
        // 10.61.71.43:9092
        // 10.164.27.139:8092,10.164.28.25:8092,10.164.27.140:8092
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.164.27.139:8092,10.164.28.25:8092,10.164.27.140:8092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG ,"test") ;
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        kafkaConsumer = new KafkaConsumer<>(props);
    }

    @Test
    public void consumer () {
        kafkaConsumer.subscribe(Arrays.asList("labelEngineWebLog_from_machineB_test002"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }
}
