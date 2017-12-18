package com.liu.base.dao;

import com.liu.base.entity.User;
import com.liu.base.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@Repository
public interface WalletDao extends JpaRepository<Wallet, Integer>{

    @Modifying
    @Query(value = "update Wallet set total = total- :total , frozen = frozen + :total, version = version+1, updateTime = current_date " +
            "where coin.id = :coinId and user.id = :userId and total > :total" )
    int buySubmit(@Param("total") double total , @Param("userId") int userId, @Param("coinId") int coinId);

    @Modifying
    @Query("update Wallet set total = total- ?1 , frozen = frozen + ?1, version = version+1, updateTime = current_date " +
            "where coin.id = ?3 and user.id = ?2 and total > ?1" )
    int sellSubmit(double total, int userId, int coinId);

    List<Wallet> findByUser(User user);
}
