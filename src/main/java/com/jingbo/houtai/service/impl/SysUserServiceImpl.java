package com.jingbo.houtai.service.impl;

import com.jingbo.houtai.dao.SysUserMapper;
import com.jingbo.houtai.dao.UserMapper;
import com.jingbo.houtai.entity.PageResult;
import com.jingbo.houtai.entity.SysUser;
import com.jingbo.houtai.entity.SysUserParam;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<String> listUserPerms(Integer userId) {
        return null;
    }

    @Override
    public SysUser getUserByName(String username) {
        return sysUserMapper.getUserByName(username);
    }

    @Override
    public PageResult<SysUser> getAllSysUser(SysUserParam sysUserParam) {
        Integer offset = (sysUserParam.getPageIndex() - 1) * sysUserParam.getPageSize();
        sysUserParam.setOffset(offset);
        List<SysUser> allUser = sysUserMapper.getAllSysUser(sysUserParam);
        Integer countUser = sysUserMapper.getCountSysUser(sysUserParam);
        PageResult pageResult = new PageResult();
        pageResult.setResults(allUser);
        pageResult.setPageNo(sysUserParam.getPageIndex());
        pageResult.setPageSize(sysUserParam.getPageSize());
        pageResult.setTotalRecords(Long.valueOf(countUser));
        return pageResult;
    }

    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.updateSysUser(sysUser);
    }

    @Override
    public void deleteSysUser(SysUser sysUser) {
        sysUserMapper.deleteSysUser(sysUser);
    }

    @Override
    public SysUser getSysUserByid(Integer userId) {
        return sysUserMapper.getSysUserByid(userId);
    }

    @Override
    public void updatePassword(SysUser sysUser) {
        sysUserMapper.updatePassword(sysUser);
    }

    @Override
    public void addSysUser(SysUser sysUser) {
        sysUserMapper.addSysUser(sysUser);
    }

    @Override
    public List<SysUser> getUserByAccountType(Integer accountType) {
        return sysUserMapper.getUserByAccountType(accountType);
    }

    @Override
    public void batchdeleteSysUser(List<String> sysUserIds) {
        sysUserMapper.batchdeleteSysUser(sysUserIds);
    }

    @Override
    public List<String> getNameByCreateId(Integer userId) {
        return sysUserMapper.getNameByCreateId(userId);
    }

    @Override
    public List<String> getNameByCreateIdAndService(Integer userId) {
        return sysUserMapper.getNameByCreateIdAndService(userId);
    }
}
