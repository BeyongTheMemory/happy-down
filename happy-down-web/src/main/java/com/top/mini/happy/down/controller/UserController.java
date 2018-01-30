package com.top.mini.happy.down.controller;

import com.top.mini.happy.down.dto.ScoreRankDTO;
import com.top.mini.happy.down.form.AddScoreForm;
import com.top.mini.happy.down.form.AddUserForm;
import com.top.mini.happy.down.form.PageForm;
import com.top.mini.happy.down.mybatis.Pageable;
import com.top.mini.happy.down.response.Result;
import com.top.mini.happy.down.service.UserScoreService;
import com.top.mini.happy.down.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author xugang on 18/1/26.
 */
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private UserScoreService userScoreService;


    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result addUser(@RequestBody @Valid AddUserForm form) {
        return new Result();
    }

    @PostMapping(value = "/score", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result addScore(@RequestBody @Valid AddScoreForm form) {
        userScoreService.save(form.getUnionId(),form.getScore());
        return new Result();
    }

    @GetMapping(value = "/score")
    public Result getScore() {
        return new Result();
    }

    @GetMapping(value = "/score/rank")
    public Result<List<ScoreRankDTO>> getRank(@Valid PageForm form) {
        Result result = new Result();
        Pageable pageable = new Pageable();
        pageable.setPageNumber(form.getPageNumber());
        pageable.setPageSize(form.getPageSize());
        List<ScoreRankDTO> scores = userScoreService.getRank(pageable);
        result.setData(scores);
        return result;
    }


}
