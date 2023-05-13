package com.schema.schemaDemo.controller;

import com.schema.schemaDemo.commander.UserActionCommander;
import com.schema.schemaDemo.model.UserActionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserActionCommander userActionCommander;

    @GetMapping("/by-uuid")
    Optional<String> byUuid(@RequestParam Long uuid) {
        userActionCommander.execute(UserActionModel.builder().Action("cdcd").key(UUID.randomUUID().toString()).build(),false);
        return Optional.of("dc");
    }

}
