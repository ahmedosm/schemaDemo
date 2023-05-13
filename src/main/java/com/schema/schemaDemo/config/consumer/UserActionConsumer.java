package com.schema.schemaDemo.config.consumer;

import com.schema.schemaDemo.model.MyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class UserActionConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserActionConsumer.class);

   // @KafkaListener(topics = "${kafka.topic.userActionTopic}",containerFactory = "kafkaListenerContainerFactory")
   // public void receive(Object payload) {
     //   LOGGER.info("Received payload1='{}'", payload);
   // }

    @RetryableTopic(
            kafkaTemplate = "kafkaTemplate2",
            attempts = "3",
            retryTopicSuffix = "-retry",
            dltTopicSuffix = "-dlt",
            backoff = @Backoff(delayExpression = "10000"),
            fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC,
            include = {RuntimeException.class,Exception.class},
            timeout = "60000",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = "${kafka.topic.userActionTopic}",containerFactory = "kafkaListenerContainerFactory2")
    public void receive2(MyEvent payload) {
        LOGGER.info("Received payload2='{}'", payload);
    }

}