package com.schema.schemaDemo.config.producer;
import com.schema.schemaDemo.model.MyEvent;
import com.schema.schemaDemo.service.FallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@EnableAsync
public class UserActionProducer {

    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    @Qualifier("kafkaTemplate2")
    private KafkaTemplate<String, MyEvent> kafkaTemplate2;
    @Autowired
    FallBackService fallBackService;


    @Async
    public void send(String topic, String key, Object payload, Boolean isFallback) {
        try {
            log.info("Sending payload='{}' to topic='{}'", payload, topic);
            ListenableFuture<SendResult<String, Object>> sendResultListenableFuture = kafkaTemplate.send(topic, key, payload);
            sendResultListenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                public void onSuccess(final SendResult<String, Object> message) {
                    log.info("sent message= " + message + " with offset= " + message.getRecordMetadata().offset());
                    fallBackService.updateFallBack(key,isFallback);

                }

                public void onFailure(final Throwable throwable) {
                    log.error("onFailure unable to send message= ", throwable);
                    fallBackService.onFailureFallBack(topic,key,payload,isFallback);

                }
            });
        } catch (Exception e) {
            log.error("unable to send message= {} ", e.getCause());
            fallBackService.onFailureFallBack(topic,key,payload,isFallback);
        }

    }

   /* @Async
    public void send2(String topic, String key, MyEvent payload) {
        try {
            log.info("Sending payload='{}' to topic='{}'", payload, topic);
            ListenableFuture<SendResult<String, MyEvent>> sendResultListenableFuture = kafkaTemplate2.send(topic, key, payload);
            sendResultListenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, MyEvent>>() {
                public void onSuccess(final SendResult<String, MyEvent> message) {
                    log.info("sent message= " + message + " with offset= " + message.getRecordMetadata().offset());
                }

                public void onFailure(final Throwable throwable) {
                    saveCallBack(key,payload);
                    log.error("unable to send message= ", throwable);
                }
            });
        } catch (Exception e) {
            log.error("unable to send message= ", e);
            saveCallBack(key,payload);
        }

    }*/

}