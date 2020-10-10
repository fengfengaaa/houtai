package com.jingbo.houtai.dao;

import com.jingbo.houtai.entity.APK;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApkMapper {
    APK getCurrentApk();

    void insertAPK(APK apk);
}
