package com.example.messenger_service.configs;

import com.example.messenger_service.events.FriendshipCreated;
import com.example.messenger_service.events.FriendshipDeleted;
import com.example.messenger_service.events.UserCreated;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

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
    public ConcurrentKafkaListenerContainerFactory<String, UserCreated> createdUserKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserCreated> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ConsumerFactory(UserCreated.class));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FriendshipCreated> createdFriendshipKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FriendshipCreated> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ConsumerFactory(FriendshipCreated.class));
        return factory;
    }

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, FriendshipDeleted> deletedFriendshipKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, FriendshipDeleted> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(ConsumerFactory(FriendshipDeleted.class));
//        return factory;
//    }
}
