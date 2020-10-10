package com.jingbo.houtai.service;

import com.jingbo.houtai.entity.PageResult;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserParam;
import com.jingbo.houtai.entity.UserPrice;

import java.util.List;

public interface UserPriceService {

    void batchAddUserPrice(List<UserPrice> userPrices);

    List<UserPrice> getUserPriceByUserid(Integer id);

    void batchAddOrUpdateUserPrice(List<UserPrice> userPrices);
}
