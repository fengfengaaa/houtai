package com.jingbo.houtai.dao;


import com.jingbo.houtai.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser getUserByName(String name);

    List<SysUser> getAllSysUser(SysUserParam param);

    Integer getCountSysUser(SysUserParam param);

    void updateSysUser(SysUser sysUser);

    void deleteSysUser(SysUser sysUser);

    SysUser getSysUserByid(Integer userId);

    void updatePassword(SysUser sysUser);

    void addSysUser(SysUser sysUser);

    List<SysUser> getUserByAccountType(Integer accountType);

    void batchdeleteSysUser(List<String> sysUserIds);

    List<String> getNameByCreateId(Integer userId);

    List<String> getNameByCreateIdAndService(Integer userId);
}
