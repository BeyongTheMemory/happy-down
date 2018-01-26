package com.top.mini.happy.down.entity;


import lombok.Data;

/**
 * @author xugang
 */
@Data
public class UserEntity{
    private Integer id;

    private String openId;

    private String unionId;

    private String nickName;

    /**
     * 性别，0：未知、1：男、2：女
     */
    private Byte gender;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;

}