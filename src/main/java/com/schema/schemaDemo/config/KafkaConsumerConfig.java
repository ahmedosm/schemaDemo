package com.schema.schemaDemo.config;
import com.schema.schemaDemo.model.MyEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kafka cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
       // props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // allows a pool of processes to divide the work of consuming and processing records
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "schemaz");
        // automatically reset the offset to the earliest offset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return props;
    }




    /*
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>(Object.class);
        jsonDeserializer.addTrustedPackages("*");
        // jsonDeserializer.addTrustedPackages(COM_GOTRAH_MESSAGING_DTO);
        jsonDeserializer.ignoreTypeHeaders();
        //
        ErrorHandlingDeserializer<Object> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);
        errorHandlingDeserializer.setFailedDeserializationFunction((FailedDeserializationInfo details)->{
            handleDeserializationException(details);
            return null;

        });
       // return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(),errorHandlingDeserializer);
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    @Bean
    @Primary
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
    */
    @Bean
    public ConsumerFactory<String, MyEvent> consumerFactory2() {
       JsonDeserializer<MyEvent> jsonDeserializer = new JsonDeserializer<>(MyEvent.class);
        jsonDeserializer.addTrustedPackages("*");
        // jsonDeserializer.addTrustedPackages(COM_GOTRAH_MESSAGING_DTO);
        jsonDeserializer.ignoreTypeHeaders();
        ErrorHandlingDeserializer<MyEvent> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);
        errorHandlingDeserializer.setFailedDeserializationFunction((FailedDeserializationInfo details)->{
            handleDeserializationException(details);
            return null;

        });
        // return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(),errorHandlingDeserializer);
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());


    }

    @Bean
    @Primary
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MyEvent>> kafkaListenerContainerFactory2() {
        ConcurrentKafkaListenerContainerFactory<String, MyEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory2());
        return factory;
    }


    private void handleDeserializationException(FailedDeserializationInfo details) {
        log.error(" can't parse the message ");
        //log.error(MessageFormat.format("error to parse object ",new String(details.getData()) ), details.getException());
    }


}