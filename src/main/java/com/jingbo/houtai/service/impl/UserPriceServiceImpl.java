package com.jingbo.houtai.service.impl;

import com.jingbo.houtai.dao.UserPriceMapper;
import com.jingbo.houtai.entity.UserPrice;
import com.jingbo.houtai.service.UserPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserPriceServiceImpl implements UserPriceService {
    @Autowired
    private UserPriceMapper userPriceMapper;

    @Override
    public void batchAddUserPrice( List<UserPrice> userPrice) {
        userPriceMapper.batchAddUserPrice(userPrice);
    }

    @Override
    public List<UserPrice> getUserPriceByUserid(Integer id) {
        return userPriceMapper.getUserPriceByUserid(id);
    }

    @Override
    @Transactional
    public void batchAddOrUpdateUserPrice(List<UserPrice> userPrices) {
        List<UserPrice> update = new ArrayList<>();
        List<UserPrice> add = new ArrayList<>();
        userPrices.forEach(item -> {
            Integer project = item.getProject();
            Integer userId = item.getUserId();
            UserPrice userPriceByUseridAndProject = userPriceMapper.getUserPriceByUseridAndProject(project, userId);
            if(userPriceByUseridAndProject != null){
                update.add(item);
            }else{
                add.add(item);
            }
        });
        if(add.size() > 0){
            userPriceMapper.batchAddUserPrice(add);
        }
        if(update.size() > 0){
            userPriceMapper.batchUpdateUserPriceById(update);
        }
    }

}
