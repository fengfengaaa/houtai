package com.jingbo.houtai.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * sys_role 实体类
 * Created by 小柒2012
 * Sun Oct 27 13:02:41 CST 2019
 */
@Getter
@Setter
public class SysRole implements Serializable{
   /**
    * 角色id 
    */
	private Long roleId;

   /**
    * 角色名称 
    */ 
	private String roleName;

   /**
    * 角色标识 
    */ 
	private String roleSign;

   /**
    * 备注 
    */ 
	private String remark;

   /**
    * 创建用户id 
    */ 
	private Long userIdCreate;

   /**
    * 创建时间 
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp gmtCreate;

   /**
    * 修改时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp gmtModified;

    private List<SysMenu> menus;
}

