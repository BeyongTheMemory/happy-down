package com.top.mini.happy.down.service.impl;

import com.top.mini.happy.down.cache.anotion.NeedCache;
import com.top.mini.happy.down.dal.UserDAO;
import com.top.mini.happy.down.dto.UserDTO;
import com.top.mini.happy.down.entity.UserEntity;
import com.top.mini.happy.down.redis.RedisKeyEnum;
import com.top.mini.happy.down.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xugang on 18/1/26.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    @Override
    public void save(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO,userEntity);
        userDAO.insertSelective(userEntity);
    }

    @Override
    @NeedCache(redisEnum = RedisKeyEnum.USER,value = "#unionId")
    public UserDTO getByUnionId(String unionId) {
        UserDTO userDTO = null;
        UserEntity userEntity = userDAO.selectByUnionId(unionId);
        if(userEntity != null){
            userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity,userDTO);
        }
        return userDTO;
    }

}
