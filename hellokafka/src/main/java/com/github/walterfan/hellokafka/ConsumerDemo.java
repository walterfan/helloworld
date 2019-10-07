package com.github.walterfan.hellokafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Author: Walter Fan
 * @Date: 3/10/2019, Thu
 **/
@Slf4j
public class ConsumerDemo {

    public static void main(String[] args) throws Exception {
        //Assign topicName to string variable
        String topicName = "walter-test-topic";
        if (args.length > 0) {
            topicName = args[0].toString();
        }

        String brokerAddress = "192.168.1.6:9092";
        if (args.length > 1) {
            brokerAddress = args[1].toString();
        }

        Properties props = new Properties();

        props.put("bootstrap.servers", brokerAddress);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer
                <String, String>(props);

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(topicName));

        //print the topic name
        System.out.println("Subscribed to topic " + topicName);
        int i = 0;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                // print the offset,key and value for the consumer records.
                i++;
                log.info("offset = {}, key = {}, value = {}\n", record.offset(), record.key(), record.value());
            }
            if(i > 10) {
                break;
            }
        }
    }
}
