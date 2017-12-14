package com.liu.base.dao;

import com.liu.base.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@Repository
public interface MarketDao extends JpaRepository<Market, Integer>{
    List<Market> findByStatusAndTradeStatus(int status, int tradeStatus);

}
