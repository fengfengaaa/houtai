package com.jingbo.houtai.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 *
 * @author liufeng
 * @date 2020/8/10 0010 22:59
 */
@Getter
@Setter
public class User extends BaseUser{
    @NotBlank(message = "请输入公司名称")
    private String companyName;//公司名称
    @NotBlank(message = "请输入地址")
    private String address;//地址
    @DateTimeFormat(pattern = "%Y-%m-%d %H:%i:%s")
    private String createDate;
    @DateTimeFormat(pattern = "%Y-%m-%d %H:%i:%s")
    private String updateDate;
    @NotBlank(message = "请选择权限")
    private String privilege; //0禁用 1采集&报告 2显示结果
    private String headPortrait;//头像
    @NotBlank(message = "服务商不能为空")
    private String sysUserName;//对应服务商账号
    private List<UserPrice> userPrices;//价格
}
