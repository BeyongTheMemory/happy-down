package com.top.mini.happy.down.dal;

import com.top.mini.happy.down.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * @author xugang
 */
@Repository
public interface UserDAO {

    int deleteByPrimaryKey(Integer id);

    int insert(UserEntity record);

    int insertSelective(UserEntity record);

    UserEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserEntity record);

    int updateByPrimaryKey(UserEntity record);
}