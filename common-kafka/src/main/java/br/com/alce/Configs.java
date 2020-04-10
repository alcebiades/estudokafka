package br.com.alce;

public class Configs {
    // Nao pode misturar o ponto (.) com underline (_) no nome do topico, pois o kafka nao funciona bem com os dois
    // juntos no nome do topico, se for usar algum, voce deve usar um ou outro, nunca os dois juntos.
    public static final String SALE_TOPIC_NAME = "TOPIC_SALE";
    public static final String EMAIL_TOPIC_NAME = "TOPIC_EMAIL";
    public static final String BROKER_URL = "localhost:9092";
}
