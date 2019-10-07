package com.github.walterfan.hellokafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

@Slf4j
public class Producer {

    public static void main(String[] args){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "10.224.77.178:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer kafkaProducer = new KafkaProducer(properties);
        try{
            for(int i = 0; i < 100; i++){
                log.info("%d", i);
                kafkaProducer.send(new ProducerRecord("waltertest", Integer.toString(i), "test message - " + i ));
            }
        }catch (Exception e){
            log.error("send kafka error", e);
        }finally {
            kafkaProducer.close();
        }
    }
}