package com.github.walterfan.hellokafka;

import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Walter Fan
 * @Date: 7/9/2019, Sat
 **/
@RestController
@RequestMapping(value = "/kafka")
public class DemoController {

        @Autowired
        private KafkaClient kafkaClient;


        @PostMapping(value = "/publish")
        public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
            kafkaClient.sendMessage(message);
        }
    }
