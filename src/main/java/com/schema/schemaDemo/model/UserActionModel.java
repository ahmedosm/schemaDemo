package com.schema.schemaDemo.model;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserActionModel {
    private String key;
    private  String Action;


}
