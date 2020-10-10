package com.jingbo.houtai.service;

import com.jingbo.houtai.entity.*;

import java.util.List;

public interface SysUserService {
    List<String> listUserPerms(Integer userId);

    SysUser getUserByName(String username);

    PageResult<SysUser> getAllSysUser(SysUserParam sysUserParam);

    void updateSysUser(SysUser sysUser);

    void deleteSysUser(SysUser sysUser);

    SysUser getSysUserByid(Integer userId);

    void updatePassword(SysUser sysUser);

    void addSysUser(SysUser sysUser);

    List<SysUser> getUserByAccountType(Integer accountType);

    void batchdeleteSysUser(List<String> sysUserIds);

    List<String> getNameByCreateId(Integer userId);
    /**
    * 普通root创建的服务商
    * @author liufeng
    * @date 2020/9/27 0027 23:13
     */
    List<String> getNameByCreateIdAndService(Integer userId);
}
