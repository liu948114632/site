package com.liu.base.dao;

import com.liu.base.entity.InAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InAddressDao extends JpaRepository<InAddress, Integer> {
    InAddress findInAddressByUserIdAndCoinId(int userId ,int coinId);
}
