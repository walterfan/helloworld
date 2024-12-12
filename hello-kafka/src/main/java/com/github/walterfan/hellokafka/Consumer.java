package com.github.walterfan.hellokafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Consumer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "10.224.77.178:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test-group");

        KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
        List topics = new ArrayList();
        topics.add("waltertest");
        kafkaConsumer.subscribe(topics);
        try{
            while (true){
                ConsumerRecords<String,String> records = kafkaConsumer.poll(10);
                for (ConsumerRecord<String,String> record: records){
                    log.info("Topic - {}, Partition - {}, Value: {}", record.topic(), record.partition(), record.value());
                }
            }


        }catch (Exception e){
            log.error("poll kafka message error", e);
        }finally {
            kafkaConsumer.close();
        }
    }
}