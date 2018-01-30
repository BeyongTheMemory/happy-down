package com.top.mini.happy.down.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


/**
 * @author xugang on 18/1/26.
 */
@Data
public class AddUserForm {

    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @NotBlank(message = "头像不能为空")
    private String avatarUrl;

    @NotBlank(message = "性别不能为空")
    private String gender;

    private String city;

    private String province;

    private String country;

    @NotBlank(message = "原始数据字符串不能为空")
    private String rawData;

    @NotBlank(message = "签名校验不能为空")
    private String signature;

    @NotBlank(message = "加密数据不能为空")
    private String encryptedData;

    @NotBlank(message = "加密偏移量不能为空")
    private String iv;

}
