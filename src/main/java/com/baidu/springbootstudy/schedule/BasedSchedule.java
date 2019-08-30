package com.baidu.springbootstudy.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BasedSchedule {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(cron="0 0 * * * ? ")
    private void test () {
        String time = this.format.format(new Date());
        kafkaTemplate.send("labelEngineWebLog_from_machineB_test001", "current time is " + time);
    }
}
