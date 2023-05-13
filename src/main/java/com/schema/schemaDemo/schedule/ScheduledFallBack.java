package com.schema.schemaDemo.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
@Slf4j
public class ScheduledFallBack {

    @Autowired
    private FallBackScheduleTask fallBackScheduleTask;
    @Async
    @Scheduled(fixedRate = 1*60*1000)
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        fallBackScheduleTask.executeFallBackTask(System.currentTimeMillis());

    }


}
