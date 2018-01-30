package com.top.mini.happy.down.eventbus.subscribe;

import com.google.common.eventbus.Subscribe;
import com.top.mini.happy.down.entity.UserScoreEntity;
import com.top.mini.happy.down.eventbus.EventBusHolder;
import com.top.mini.happy.down.redis.RedisKeyEnum;
import com.top.mini.happy.down.redis.RedisOperate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Tuple;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

/**
 * @author xugang on 18/1/29.
 */
@Component
public class NewScore {
    @Resource
    private EventBusHolder eventBusHolder;


    @Resource
    private RedisOperate redis;

    /**
     * 排行榜取前100名
     * 该变量用于保存第100名得分
     */
    private int lowestScore = 0;

    /**
     * 最低分在redis中的下标
     */
    private int lowestIndex = 99;


    @PostConstruct
    public void init() {
        eventBusHolder.getAsyncEventBus().register(this);
    }

    @Subscribe
    public void newScore(UserScoreEntity userScoreEntity) {
        if (userScoreEntity.getScore() == null || userScoreEntity.getScore() < 1) {
            return;
        }
        //最低分存储在redis中，尝试载入
        if (lowestScore < 1) {
            Set<Tuple> result = redis.zrevrangeWithScores(RedisKeyEnum.SCORE_RANK.getKey(), lowestIndex, lowestIndex);
            if (!CollectionUtils.isEmpty(result)) {
                result.forEach(tuple -> lowestScore = (int) tuple.getScore());
            }
        }
        //不能低于最低分
        if (userScoreEntity.getScore() <= lowestScore) {
            return;
        }
        //判断当前用户是否已在排行榜中
        Double score = redis.zscore(RedisKeyEnum.SCORE_RANK.getKey(),userScoreEntity.getUnionId());
        if(score != null){
            //判断分数
            if (score >= userScoreEntity.getScore()){
                return;
            }
        }
        //更新得分
        redis.zsetAdd(RedisKeyEnum.SCORE_RANK.getKey(),userScoreEntity.getScore(),userScoreEntity.getUnionId());
    }

}
