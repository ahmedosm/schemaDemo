package com.schema.schemaDemo.schedule;
import com.schema.schemaDemo.commander.UserActionCommander;
import com.schema.schemaDemo.entity.FallBack;
import com.schema.schemaDemo.mapper.MappingHelper;
import com.schema.schemaDemo.service.FallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class FallBackScheduleTask {
    @Autowired
    private UserActionCommander userActionCommander;

    @Autowired
    private FallBackService fallBackNodeService;

    @Autowired
    private MappingHelper mappingHelper;

    public void executeFallBackTask(Long currentDateTime) {
        log.info("fallback FallBackScheduleTask start execution ", currentDateTime);
        List<FallBack> allFallBack = fallBackNodeService.getFallBackByStatus("Open");
        log.info("fallback excustion task list size = {} ", allFallBack.size());
        allFallBack.stream()
                .forEach(this::processFallBackSubmit);

    }

    private void processFallBackSubmit(FallBack fallBackNode) {
        try {
            Object readFromJson = mappingHelper.readFromJson(fallBackNode.getJsonModel(), MappingHelper.getClassByName(fallBackNode.getObjectHeader()));
            userActionCommander.execute(fallBackNode.getTopicName()
                    , fallBackNode.getKey(), readFromJson, true);
            log.info("Fallback task execute submitted successfully key {} ", fallBackNode.getKey());

        } catch (Exception e) {
            fallBackNode.setReTryCount(fallBackNode.getReTryCount() + 1);
            fallBackNode.setStatus("Open");
            fallBackNodeService.saveFallBack(fallBackNode);
            log.error("error to send fall back");
        }


    }
}
