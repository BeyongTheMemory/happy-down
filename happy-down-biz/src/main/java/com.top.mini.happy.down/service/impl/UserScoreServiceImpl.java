package com.top.mini.happy.down.service.impl;

import com.top.mini.happy.down.dal.UserScoreDAO;
import com.top.mini.happy.down.dto.ScoreRankDTO;
import com.top.mini.happy.down.dto.UserDTO;
import com.top.mini.happy.down.entity.UserScoreEntity;
import com.top.mini.happy.down.eventbus.EventBusHolder;
import com.top.mini.happy.down.mybatis.Pageable;
import com.top.mini.happy.down.redis.RedisKeyEnum;
import com.top.mini.happy.down.redis.RedisOperate;
import com.top.mini.happy.down.service.UserScoreService;
import com.top.mini.happy.down.service.UserService;
import com.top.mini.happy.down.util.CountUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Tuple;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author xugang on 18/1/26.
 */
@Service
public class UserScoreServiceImpl implements UserScoreService {

    @Resource
    private EventBusHolder eventBusHolder;

    @Resource
    private UserScoreDAO userScoreDAO;

    @Resource
    private UserService userService;

    @Resource
    private RedisOperate redis;

    @PostConstruct
    public void init(){
        eventBusHolder.getAsyncEventBus().register(this);
    }

    @Override
    public void save(String unionId,Integer score) {
        UserScoreEntity userScoreEntity = new UserScoreEntity();
        userScoreEntity.setUnionId(unionId);
        userScoreEntity.setScore(score);
        userScoreDAO.insertSelective(userScoreEntity);
        eventBusHolder.getAsyncEventBus().post(userScoreEntity);
    }

    @Override
    public List<ScoreRankDTO> getRank(Pageable pageable) {
        List<ScoreRankDTO> scores = new ArrayList<>();
        int begin = pageable.getOffset();
        int end = pageable.getOffset() +  ( pageable.getPageSize() - 1 );
        Set<Tuple> result = redis.zrevrangeWithScores(RedisKeyEnum.SCORE_RANK.getKey(),begin,end);
        //排行榜中排名
        CountUtil.Count rank = CountUtil.getCount(pageable.getOffset() + 1);
        if(!CollectionUtils.isEmpty(result)){
            result.forEach(tuple -> {
                ScoreRankDTO score = new ScoreRankDTO();
                score.setRank(rank.getNum());
                score.setScore((int)tuple.getScore());
                score.setUnionId(tuple.getElement());
                //获取用户信息
                UserDTO user = userService.getByUnionId(score.getUnionId());
                score.setNickName(user.getNickName());
                score.setAvatarUrl(user.getAvatarUrl());
                scores.add(score);
                rank.add();
            });

        }
        return scores;
    }
}
