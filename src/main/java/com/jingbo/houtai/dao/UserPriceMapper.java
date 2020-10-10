package com.jingbo.houtai.dao;

import com.jingbo.houtai.entity.UserPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserPriceMapper {
    void batchAddUserPrice( List<UserPrice> userPrices);

    List<UserPrice> getUserPriceByUserid(Integer id);

    void batchUpdateUserPriceById(List<UserPrice> userPrices);

    UserPrice getUserPriceByUseridAndProject(@Param("project")Integer project, @Param("userId")Integer userId);
}
