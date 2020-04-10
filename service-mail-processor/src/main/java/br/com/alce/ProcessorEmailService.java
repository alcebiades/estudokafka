package br.com.alce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class ProcessorEmailService {

    public static void main(String[] args) {

        var processorEmailService = new ProcessorEmailService();

        try (var listener = new KafkaListener(Configs.EMAIL_TOPIC_NAME, getProperties(), processorEmailService::process)) {
            listener.run();
        }
    }

    private void process(final ConsumerRecord<String, String> email) {
        System.out.println("\nEMAIL:");
        System.out.println("Chave: " + email.key());
        System.out.println("Mensagem: " + email.value());
        System.out.println("Offset: " + email.offset());
        System.out.println("-------------------------");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Properties getProperties() {
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Configs.BROKER_URL);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Define qual grupo pertence os consumidores, se mais de um consumidor pertencer ao grupo informado,
        // eles vao paralelizar o consumo das mensagens
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ProcessorEmailService.class.getSimpleName());
        // Define a quantidade maxima de mensagens que o poll consome por vez
        // Foi definado 1 para que a cada mensagem seja realizado o commite, com isso caso haja rebalanceamento de
        // consumidor, a chance das mensagens se perder e menor
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return properties;
    }
}
