package com.jingbo.houtai.service.impl;

import com.jingbo.houtai.dao.ApkMapper;
import com.jingbo.houtai.dao.UserMapper;
import com.jingbo.houtai.entity.APK;
import com.jingbo.houtai.entity.PageResult;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserParam;
import com.jingbo.houtai.service.ApkService;
import com.jingbo.houtai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApkServiceImpl implements ApkService {
    @Autowired
    private ApkMapper apkMapper;

    @Override
    public APK getCurrentApk() {
        return apkMapper.getCurrentApk();
    }

    @Override
    public void insertAPK(APK apk) {
        apkMapper.insertAPK(apk);
    }
}
