package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SysUserParam {
    private String userName;
    private String phoneNum;
    @NotNull
    private Integer pageSize = 10;
    @NotNull
    private Integer pageIndex = 1;
    private Integer offset;
    private Integer accountType;
    private Integer userIdCreate;
}
