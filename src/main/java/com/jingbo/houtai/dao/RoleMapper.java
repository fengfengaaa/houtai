package com.jingbo.houtai.dao;


import com.jingbo.houtai.entity.SysRole;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    SysRole getRoleByRoleId(Integer roleId);
}

