package com.schema.schemaDemo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class MyEvent {

    private String evnetId;
    private  String eventType;
    private String eventSubType;

}
