package br.com.alce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;
import java.util.regex.Pattern;

public class ProcessorLogService {

    public static void main(String[] args) {

        var processorLogService = new ProcessorLogService();

        try (var listener = new KafkaListener(Pattern.compile("TOPIC.*"), getProperties(), processorLogService::process)) {
            listener.run();
        }
    }

    private void process(final ConsumerRecord<String, String> log) {
        System.out.println("\nLOG:");
        System.out.println("Chave: " + log.key());
        System.out.println("Mensagem: " + log.value());
        System.out.println("Offset: " + log.offset());
        System.out.println("-------------------------");
    }

    private static Properties getProperties() {
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Configs.BROKER_URL);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Define qual grupo pertence os consumidores, se mais de um consumidor pertencer ao grupo informado,
        // eles vao paralelizar o consumo das mensagens
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ProcessorLogService.class.getSimpleName());
        // Define a quantidade maxima de mensagens que o poll consome por vez
        // Foi definado 1 para que a cada mensagem seja realizado o commite, com isso caso haja rebalanceamento de
        // consumidor, a chance das mensagens se perder e menor
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return properties;
    }
}
