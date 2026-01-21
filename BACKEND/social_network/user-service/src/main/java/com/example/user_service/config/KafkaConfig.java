package com.example.user_service.config;

import com.example.user_service.event.UserCreated;
import com.example.user_service.event.UserDeleted;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${kafka.group.id}")
    private String GROUP_ID;

    @Value("${kafka.server}")
    private String SERVER;


    private <T> ConsumerFactory<String, T> ConsumerFactory(Class<T> tClass) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(tClass, false);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserCreated> createdKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserCreated> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ConsumerFactory(UserCreated.class));
        return factory;
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDeleted> deletedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserDeleted> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ConsumerFactory(UserDeleted.class));
        return factory;
    }
}
