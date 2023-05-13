package com.schema.schemaDemo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.retry.annotation.Backoff;

//@Configuration
//@EnableKafka
public class KafkaRetryConfiguration {

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 1000, maxDelay = 5000))
    public NewTopic myRetryableTopic() {
        return TopicBuilder.name("lol-retry")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
