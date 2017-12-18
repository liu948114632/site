package com.liu.base.dao;

import com.liu.base.entity.Coin;
import com.liu.base.entity.User;
import com.liu.base.entity.VirtualOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirtualOperationDao extends JpaRepository<VirtualOperation, Integer> {
    List<VirtualOperation> findByUserAndCoinaAndType(User user, Coin coin, int type);
 }
