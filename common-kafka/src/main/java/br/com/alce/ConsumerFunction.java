package br.com.alce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction<T> {

    void process(ConsumerRecord<String, T> record);
}
