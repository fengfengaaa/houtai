package com.jingbo.houtai.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * sys_user 系统用户
 * Created by 小柒2012
 * Sun Oct 27 13:03:00 CST 2019
 */
@Data
public class SysUser extends BaseUser implements Serializable{
   /**
    * 姓名(昵称) 
    */
	private String nickname;

   /**
    * 邮箱 
    */
	private String email;

   /**
    * 头像上传 0:未上传 1:上传 
    */
	private Short avatarStatus;

   /**
    * 备注 
    */
	private String remark;
    @NotNull(message = "账号类型不能为空")
	private Integer accountType;//账号类型 0 root 1 运营账号 2 财务账号 3 制作账号 4 快递账号 5 服务商账号 6普通root 7 制作服务商 其实就是role 目前只设置一个role

/*    private String role;*/

    @DateTimeFormat(pattern = "%Y-%m-%d %H:%i:%s")
    private String createDate;
    @DateTimeFormat(pattern = "%Y-%m-%d %H:%i:%s")
    private String updateDate;

    private Integer userIdCreate;//所属用户姓名Id
    public Boolean isAdmin(){
        return accountType == 0 ? true : false;
    }
}

