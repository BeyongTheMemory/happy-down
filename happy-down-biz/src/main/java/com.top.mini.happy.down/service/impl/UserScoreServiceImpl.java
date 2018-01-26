package com.top.mini.happy.down.service.impl;

import com.top.mini.happy.down.dal.UserScoreDAO;
import com.top.mini.happy.down.dto.UserScoreDTO;
import com.top.mini.happy.down.entity.UserScoreEntity;
import com.top.mini.happy.down.service.UserScoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xugang on 18/1/26.
 */
@Service
public class UserScoreServiceImpl implements UserScoreService {

    @Resource
    private UserScoreDAO userScoreDAO;

    @Override
    public void save(UserScoreDTO userScoreDTO) {
        UserScoreEntity userScoreEntity = new UserScoreEntity();
        BeanUtils.copyProperties(userScoreEntity,userScoreDTO);
        userScoreDAO.insertSelective(userScoreEntity);
    }
}
