package com.schema.schemaDemo.service.impl;
import com.schema.schemaDemo.entity.FallBack;
import com.schema.schemaDemo.mapper.MappingHelper;
import com.schema.schemaDemo.repository.FallBackRepository;
import com.schema.schemaDemo.service.FallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FallBackServiceImpl implements FallBackService {
    @Autowired
    private FallBackRepository repository;
    @Value("${fallback.reTryCount}")
    private Integer configReTryCount;
    @Autowired
    private MappingHelper mappingHelper;

    @Override
    public void saveFallBack(FallBack fallBackNode) {
        log.info("saving fallback");
        repository.save(fallBackNode);

    }

    @Override
    public Optional<FallBack> getFallBackBYKey(String key) {
        log.info("get by key ", key);
        return repository.findById(key);

    }

    @Override
    public List<FallBack> getFallBackByStatus(String status) {
        log.info("find by status :{} , configReTryCount ={}  ", status, configReTryCount);
        return repository.findByStatusAndReTryCountLessThanEqual(status, configReTryCount);

    }

    @Override
    public void updateFallBack(String key, Boolean isFallback) {
        log.info("on success update fallback for key : {}  ", key);
        if (isFallback) {
            Optional<FallBack> fallBackBYKey = getFallBackBYKey(key);
            fallBackBYKey.ifPresent(value -> {
                value.setStatus("Closed");
                saveFallBack(value);
            });
        }
    }

    @Override
    public void onFailureFallBack(String topicName,String key, Object payload, Boolean isFallback) {
        log.info("onFailureFallBack for key : {} ", key);
        if (isFallback) {
            Optional<FallBack> fallBackBYKey = getFallBackBYKey(key);
            fallBackBYKey.ifPresent(value -> {
                value.setReTryCount(value.getReTryCount() + 1);
                value.setStatus("Open");
                saveFallBack(value);
            });

        } else {
            log.info("unable to send message= {} then it will be saved in fallback  ", payload);
            saveFallBack(FallBack.builder().key(key).jsonModel(mappingHelper.mapperObjectToJson(payload))
                    .topicName(topicName).reTryCount(1).status("Open").objectHeader(payload.getClass().getName()).build());
        }

    }
}
