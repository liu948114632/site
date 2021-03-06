package com.liu.base.dao;

import com.liu.base.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@Repository
public interface OrderDao extends JpaRepository<Orders, Integer>{
}
