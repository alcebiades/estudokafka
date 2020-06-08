package br.com.alce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static br.com.alce.Configs.BROKER_URL;

public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, T> producer;

    KafkaDispatcher() {
        this.producer = new KafkaProducer<>(getProperties());
    }

    private static Properties getProperties() {
        final Properties properties = new Properties();
        // Define o endereco e a porta do broker do kafka
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_URL);
        // Define o serializador para a chave da mensagem
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Define o serializador para o corpo da mensagem
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        // Define o tipo de acknowledge que será usado na postagem da mensagem:
        // "0" (zero) o produtor nao espera que o broker confirme o recebimento da mensagem.
        // "1" o produtor espera apenas que o leader do cluster responda que a mensagem foi recebida.
        // "all" o produtor espera que o leader e suas replicar recebam a mensagem, ou seja, que eles estejam in-sync.
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }

    public void send(final String topicName, final String key, final T message) throws ExecutionException, InterruptedException {

        final var record = new ProducerRecord<>(topicName, key, message);

        final Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Topico: " + data.topic() + "/Partition: " + data.partition() + "/Offset: " + data.offset() + "/Timestamp: " + data.timestamp());
        };

        producer.send(record, callback).get();
    }

    @Override
    public void close() {
        producer.close();
    }
}
