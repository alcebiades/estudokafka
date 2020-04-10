package br.com.alce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class ProcessorSaleService<T> {

    public static void main(String[] args) {

        var processorSaleService = new ProcessorSaleService();

        try (var listener = new KafkaListener(Configs.SALE_TOPIC_NAME, getProperties(), processorSaleService::process)) {
            listener.run();
        }
    }

    private void process(final ConsumerRecord<String, T> sale) {
        System.out.println("\nVENDA:");
        System.out.println("Chave: " + sale.key());
        System.out.println("Mensagem: " + sale.value());
        System.out.println("Offset: " + sale.offset());
        System.out.println("-------------------------");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Properties getProperties() {
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Configs.BROKER_URL);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        // Criando configuracoes personalizadas
        properties.setProperty(GsonDeserializer.CLASS_TYPE_DESERIALIZER, Sale.class.getName());
        // Define qual grupo pertence os consumidores, se mais de um consumidor pertencer ao grupo informado,
        // eles vao paralelizar o consumo das mensagens
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ProcessorSaleService.class.getSimpleName());
        // Configurando um nome para o cliente
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, "ALCE");
        // Define a quantidade maxima de mensagens que o poll consome por vez
        // Foi definado 1 para que a cada mensagem seja realizado o commite, com isso caso haja rebalanceamento de
        // consumidor, a chance das mensagens se perder e menor
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");

        return properties;
    }
}
