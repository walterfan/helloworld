package com.github.walterfan.hellokafka;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface KafkaClient<P, M> extends Closeable {

    void sendMessage(String message);

    void sendMessage(P partitionKey, M message);

    Future sendMessageAsync(P partitionKey, M message, Consumer<Exception> exceptionHandler);

    void close();

}
