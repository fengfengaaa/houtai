package com.jingbo.houtai.entity;

import com.jingbo.houtai.constParam.Const;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BaseUser implements Serializable {
    private Integer userId;
    @NotBlank(message = "用户名不能为空")
    private String userName;
/*    @NotBlank(message = "联系方式不能为空")*/
    private String phoneNum;//联系方式
    private String status = "N";//用户状态 D删除 N正常
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = Const.CHECK_JS_INJECT, message = "密码格式校验失败")
    private String password;
}
