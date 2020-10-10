package com.jingbo.houtai.service.impl;

import com.jingbo.houtai.dao.FootMapper;
import com.jingbo.houtai.entity.Foot;
import com.jingbo.houtai.service.FootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FootServiceImpl implements FootService {
    @Autowired
    private FootMapper footMapper;
    @Override
    public void addFoot(Foot foot) {
        footMapper.addFoot(foot);
    }

    @Override
    public void updateFoot(Foot foot) {
        footMapper.updateFoot(foot);
    }


}
