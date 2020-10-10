package com.jingbo.houtai.service;

import com.jingbo.houtai.entity.PageResult;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserParam;

import java.util.List;

public interface UserService {

    void addUser(User user);

    User getUserByid(Integer id);

    PageResult<User> getAllUser(UserParam userParam);

    void updateUser(User user);

    void deleteUser(User user);

    void batchdeleteUser(List<String> userIds);

    void updatePassword(User user);

    User getUserByName(String name);

    List<String> getUserNameBySysUserName(String sysUserName);
}
