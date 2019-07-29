package com.baidu.springbootstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProduceController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProduceController.class);
    private static final String TOPIC = "labelEngineWebLog_from_machineB_test001";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping("send")
    public String send () {
        ListenableFuture future = this.kafkaTemplate.send(TOPIC, "你好");
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.info("成功发送消息：{}，offset=[{}]", "你好", result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable throwable) {
                logger.error("消息：{} 发送失败，原因：{}", "你好", throwable.getMessage());
            }
        });
        return "success";
    }

    @GetMapping("send/{message}")
    public void send(@PathVariable String message) {
        ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(TOPIC, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.info("成功发送消息：{}，offset=[{}]", message, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("消息：{} 发送失败，原因：{}", message, ex.getMessage());
            }
        });
    }
}
