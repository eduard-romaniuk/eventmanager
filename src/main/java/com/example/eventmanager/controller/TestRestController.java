package com.example.eventmanager.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;

@RestController
public class TestRestController {

    ObjectMapper objectMapper = initObjectMapper();

    private ObjectMapper initObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }

    @RequestMapping(path = "/getObj/{text}", method = RequestMethod.GET)
    public String getObj(@PathVariable String text) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new Obj(text));
    }

    @RequestMapping(path = "/getObjs", method = RequestMethod.GET)
    public String getObjs() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                Arrays.asList(
                        new Obj("first"),
                        new Obj("second"),
                        new Obj("third")));
    }

    @RequestMapping("/user")
    public String user(Principal user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(user);
    }
}
