package com.github.walterfan.hellokafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @Author: Walter Fan
 * @Date: 3/10/2019, Thu
 **/
@Slf4j
public class ProducerDemo {

    public static void main(String[] args) {

        String topicName = "walter-test-topic";
        if (args.length > 0) {
            topicName = args[0].toString();
        }

        String brokerAddress = "192.168.1.6:9092";
        if (args.length > 1) {
            brokerAddress = args[1].toString();
        }



        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", brokerAddress);

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<String, String>(topicName, Integer.toString(i), Integer.toString(i)));
            log.info("{}. record sent ", i);
        }

        log.info("Message sent successfully");
        producer.close();
    }
}
