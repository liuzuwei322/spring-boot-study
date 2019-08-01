package com.baidu.springbootstudy.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BasedSchedule {

    private static final Logger logger = LoggerFactory.getLogger(BasedSchedule.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(cron="0/30 * *  * * ? ")
    private void test () {
        kafkaTemplate.send("test", "你好");
    }
}
