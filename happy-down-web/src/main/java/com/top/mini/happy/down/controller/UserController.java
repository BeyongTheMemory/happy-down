package com.top.mini.happy.down.controller;

import com.top.mini.happy.down.form.AddUserForm;
import com.top.mini.happy.down.response.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

/**
 * @author xugang on 18/1/26.
 */
@RestController
public class UserController {
    @PostConstruct
    public void init(){
        System.out.println();
    }
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result addUser(@RequestBody @Valid AddUserForm form) {
        return new Result();
    }

    @PostMapping(value = "/score", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result addScore() {
        return new Result();
    }

    @GetMapping(value = "/score")
    public Result getScore(){
        return new Result();
    }


}
