package com.schema.schemaDemo.commander;

import com.schema.schemaDemo.config.producer.UserActionProducer;
import com.schema.schemaDemo.model.MyEvent;
import com.schema.schemaDemo.model.UserActionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class UserActionCommander {

    @Autowired
    private UserActionProducer sender;

    @Value("${kafka.topic.userActionTopic}")
    private String topicName;

    public void execute(UserActionModel userActionModel ,Boolean isFallBack) {
        log.info("SimpleActionProducer  is executing..." ,userActionModel.getKey());
        String eventId = UUID.randomUUID().toString();
        sender.send(topicName+"MyEvent", eventId, MyEvent.builder().eventType("lol").eventSubType("lol2").evnetId(eventId).build(),isFallBack);
        sender.send(topicName,userActionModel.getKey() ,userActionModel,isFallBack);

    }

    public void execute(String fallBackTopicName,String key,Object fallBack ,Boolean isFallBack) {
        log.info("UserActionCommander sending FallBack   is executing..." ,key);

        sender.send(fallBackTopicName,key,fallBack,isFallBack);

    }

}

