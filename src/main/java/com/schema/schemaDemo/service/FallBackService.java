package com.schema.schemaDemo.service;



import com.schema.schemaDemo.entity.FallBack;

import java.util.List;
import java.util.Optional;

public interface FallBackService {
    void saveFallBack(FallBack FallBackNode);
    Optional<FallBack> getFallBackBYKey(String key);
    List<FallBack> getFallBackByStatus(String status);
    void updateFallBack(String key,Boolean isFallback);
    void onFailureFallBack(String topicName,String key, Object payload,Boolean isFallback);
}
