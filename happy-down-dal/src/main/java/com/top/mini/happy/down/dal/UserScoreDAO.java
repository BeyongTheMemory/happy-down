package com.top.mini.happy.down.dal;

import com.top.mini.happy.down.entity.UserScoreEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScoreDAO {

    int deleteByPrimaryKey(Long id);

    int insert(UserScoreEntity record);

    int insertSelective(UserScoreEntity record);

    UserScoreEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserScoreEntity record);

    int updateByPrimaryKey(UserScoreEntity record);
}