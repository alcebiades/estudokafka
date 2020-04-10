package br.com.alce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GsonDeserializer<T> implements Deserializer<T> {

    public static final String CLASS_TYPE_DESERIALIZER = "classTypeDeserializer";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> classTypeDeserializer;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String className = String.valueOf(configs.get(CLASS_TYPE_DESERIALIZER));
        try {
            classTypeDeserializer = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        return gson.fromJson(new String(data), classTypeDeserializer);
    }
}
