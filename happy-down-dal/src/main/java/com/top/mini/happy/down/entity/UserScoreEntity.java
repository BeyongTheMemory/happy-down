package com.top.mini.happy.down.entity;

import lombok.Data;

import java.util.Date;


/**
 * @author xugang
 */
@Data
public class UserScoreEntity {
    private Long id;

    private String unionId;

    /**
     *  得分
     */
    private Integer score;

    private Date createTime;

    private Date updateTime;

}