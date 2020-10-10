package com.jingbo.houtai.service.impl;

import com.jingbo.houtai.dao.RoleMapper;
import com.jingbo.houtai.entity.SysRole;
import com.jingbo.houtai.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public SysRole getRoleByRoleId(Integer roleId) {
        return roleMapper.getRoleByRoleId(roleId);
    }
}
