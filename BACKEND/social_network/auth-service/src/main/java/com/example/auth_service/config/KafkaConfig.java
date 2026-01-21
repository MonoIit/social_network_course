package com.example.auth_service.config;

import com.example.auth_service.event.UserCreated;
import com.example.auth_service.event.UserDeleted;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.server}")
    private String SERVER;

    @Bean
    public KafkaTemplate<String, UserCreated> createdKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(UserCreated.class));
    }

    @Bean
    public KafkaTemplate<String, UserDeleted> deletedKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(UserDeleted.class));
    }

    private <T> ProducerFactory<String, T> producerFactory(Class<T> tClass) {
        JsonSerializer<T> serializer = new JsonSerializer<>();
        serializer.setAddTypeInfo(false);

        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), serializer);
    }
}
