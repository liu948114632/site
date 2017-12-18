package com.liu.base.dao;

import com.liu.base.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/12
 */
@Repository
public interface CoinDao extends JpaRepository<Coin, Integer>{
    List<Coin> findByStatus(int i);

    list<Coin> findByu
}
