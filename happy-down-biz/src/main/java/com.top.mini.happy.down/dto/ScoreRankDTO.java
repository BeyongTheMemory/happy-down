package com.top.mini.happy.down.dto;

import lombok.Data;

/**
 * @author xugang on 18/1/29.
 */
@Data
public class ScoreRankDTO {

    private String unionId;

    private Integer score;

    /**
     * 用户名
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 排名
     */
    private int rank;
}
