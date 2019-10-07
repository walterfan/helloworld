package com.github.walterfan.hellokafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @Author: Walter Fan
 * @Date: 3/10/2019, Thu
 **/
@Slf4j
public class KafkaClientImpl<P, M> implements KafkaClient<P, M> {

    private KafkaProducer<P, M> producer;
    private String topic;
    private Duration closeTimeout;
    private ExecutorService executorService;

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(P partitionKey, M message) {

        Consumer<Exception> exceptionHandler = new Consumer<Exception>() {
            @Override
            public void accept(Exception e) {
                log.error("sendMessage error", e);
            }
        };

        sendMessageAsync(partitionKey, message, exceptionHandler);
    }

    @Override
    public Future sendMessageAsync(P partitionKey, M message, Consumer<Exception> exceptionHandler) {
        ProducerRecord<P, M> record = new ProducerRecord<>(topic, partitionKey, message);

        return executorService.submit(() -> {
            Future future = producer.send(record, (metadata, exception) -> {
                if (exception != null && exceptionHandler != null) {
                    exceptionHandler.accept(exception);
                }
            });

        });
    }

    @Override
    public void close() {
        producer.close(closeTimeout.toMillis(), TimeUnit.MILLISECONDS);
    }
}
