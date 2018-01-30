package com.top.mini.happy.down.service;

import com.top.mini.happy.down.dto.ScoreRankDTO;
import com.top.mini.happy.down.mybatis.Pageable;

import java.util.List;

/**
 * @author xugang on 18/1/26.
 */

public interface UserScoreService {

    void save(String unionId,Integer score);

    List<ScoreRankDTO> getRank(Pageable pageable);
}
