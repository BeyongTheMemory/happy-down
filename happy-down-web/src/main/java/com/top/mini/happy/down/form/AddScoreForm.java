package com.top.mini.happy.down.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;


/**
 * @author xugang on 18/1/30.
 */
@Data
public class AddScoreForm {
    @NotBlank(message = "unionId不能为空")
    private String unionId;
    @NotNull(message = "分数不能为空")
    @Range(min = 0,message = "分数必须大于0")
    private Integer score;
}
