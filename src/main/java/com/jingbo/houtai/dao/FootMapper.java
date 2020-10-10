package com.jingbo.houtai.dao;


import com.jingbo.houtai.entity.Foot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FootMapper {
    void addFoot(Foot foot);
    void updateFoot(Foot foot);
    Foot getFootById(Integer footId);
}
