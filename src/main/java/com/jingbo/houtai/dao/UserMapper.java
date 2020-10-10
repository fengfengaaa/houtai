package com.jingbo.houtai.dao;


import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void addUser();

    User getUserByid(Integer id);

    List<User> getAllUser(UserParam userParam);

    void addUser(User user);

    Integer getCountUser(UserParam userParam);

    void updateUser(User user);

    void deleteUser(User user);

    void batchdeleteUser(List<String> userIds);

    void updatePassword(User user);

    User getUserByName(String name);

    List<String> getUserNameBySysUserName(String sysUserName);
}
