package com.schema.schemaDemo.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schema.schemaDemo.exception.MappingException;
import com.schema.schemaDemo.model.UserActionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;

@Component
@Slf4j
public class MappingHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        objectMapper.findAndRegisterModules();
    }

    /**
     * Map the given JSON String to the required class type.
     */
    public static <T> T readFromJson(String json, Class<T> clazz) throws MappingException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }

    public  static Class<?>  getClassByName(String className){

        try {
            Class<?> clazz = Class.forName(className);
            return clazz;
            // Use the 'clazz' object as needed
        } catch (ClassNotFoundException e) {
            log.error("can't find the class {} ",e);
            return null;
            // Handle the exception if the class is not found
        }

    }

    /**
     * Map the given Object to a JSON String.
     */
    public static String writeToJson(Object obj) throws MappingException {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }
    public String mapperObjectToJson(Object payload){
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object ");
            e.printStackTrace();
            return null;
        }
    }

    public UserActionModel mapperJsonToObject(String payload){
        try {
            if(payload!=null) {
                return objectMapper.readValue(payload, UserActionModel.class);
            }else {
                log.error("Failed to convert object ");
                return null;
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object ");
            e.printStackTrace();
            return null;
        }
    }
}
