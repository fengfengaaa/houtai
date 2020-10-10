package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserParam {
    private String userName;
    private String phoneNum;
    @NotNull
    private Integer pageSize;
    @NotNull
    private Integer pageIndex;
    private Integer offset;
    private String sysUserName;//对应服务商账号ID
    private List<String> sysUserNames;//对应服务商账号ID集合
}
