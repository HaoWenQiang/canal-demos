package com.yibao.canaldemo.controller;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Create Date: 2020/10/14
 *
 * @author D
 * @version 1.0
 */
@RestController
@RequestMapping(value = "hey")
public class TestController {
    @Autowired
    KafkaTemplate kafkaTemplate;
    @GetMapping(value = "guy")
    public String hello() {
        kafkaTemplate.send("yjmap","{'hey': 'jack'}");
        return "hello jack";
    }
}
