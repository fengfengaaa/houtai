package com.jingbo.houtai.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* 按钮实体
* @author liufeng
* @date 2020/9/12 0012 14:35
 */
@Getter
@Setter
public class SysMenu implements Serializable{
   /**
    * 菜单id 
    */
	private Long menuId;

   /**
    * 父菜单ID，一级菜单为0 
    */
	private Long parentId;

   /**
    * 菜单名称 
    */
	private String name;

   /**
    * 菜单URL 
    */
	private String url;

   /**
    * 授权(多个用逗号分隔，如：user:list,user:create) 
    */
	private String perms;

   /**
    * 类型   0：目录   1：菜单   2：按钮 
    */
	private Integer type;

   /**
    * 菜单图标 
    */
	private String icon;

   /**
    * 排序 
    */
	private Integer orderNum;

   /**
    * 创建时间 
    */
   	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp gmtCreate;

   /**
    * 修改时间
    */
	private Timestamp gmtModified;
}

