package com.jingbo.houtai.service.impl;

import com.jingbo.houtai.dao.UserMapper;
import com.jingbo.houtai.entity.PageResult;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserParam;
import com.jingbo.houtai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }

    @Override
    public User getUserByid(Integer id) {
        return userMapper.getUserByid(id);
    }

    @Override
    public PageResult<User> getAllUser(UserParam userParam) {
        Integer offset = (userParam.getPageIndex() - 1) * userParam.getPageSize();
        userParam.setOffset(offset);
        List<User> allUser = userMapper.getAllUser(userParam);
        Integer countUser = userMapper.getCountUser(userParam);
        PageResult pageResult = new PageResult();
        pageResult.setResults(allUser);
        pageResult.setPageNo(userParam.getPageIndex());
        pageResult.setPageSize(userParam.getPageSize());
        pageResult.setTotalRecords(Long.valueOf(countUser));
        return pageResult;
    }

    @Override
    public void updateUser(User user) {
        this.userMapper.updateUser(user);
    }

    @Override
    public void deleteUser(User user) {
        this.userMapper.deleteUser(user);
    }

    @Override
    public void batchdeleteUser(List<String> userIds) {
        this.userMapper.batchdeleteUser(userIds);
    }

    @Override
    public void updatePassword(User user) {
        userMapper.updatePassword(user);
    }

    @Override
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    @Override
    public List<String> getUserNameBySysUserName(String sysUserName) {
        return userMapper.getUserNameBySysUserName(sysUserName);
    }
}
