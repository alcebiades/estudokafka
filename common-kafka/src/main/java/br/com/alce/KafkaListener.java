package br.com.alce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class KafkaListener<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction function;

    public KafkaListener(final String topic, final Properties properties, final ConsumerFunction function) {
        this.function = function;
        this.consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaListener(final Pattern topicPattern, final Properties properties, final ConsumerFunction function) {
        this.function = function;
        this.consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(topicPattern);
    }

    public void run() {
        while (true) {
            final ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                for (ConsumerRecord<String, T> item : records) {
                    function.process(item);
                }
            }
        }
    }

    @Override
    public void close() {
        consumer.close();
    }
}
