package com.liu.front.service;

import com.alibaba.fastjson.JSON;
import com.liu.base.entity.Orders;
import com.liu.base.entity.OrdersData;
import com.liu.front.utils.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class SendRabbitMessage {
    @Autowired
    RabbitService rabbitService;

    public void sendOrdersToRabbit(Orders orders){
        OrdersData ordersData = new OrdersData();
        ordersData.setAmount(orders.getAmount());
        ordersData.setCount(orders.getCount());
        ordersData.setCreateTime(new Timestamp(orders.getCreateTime().getTime()));
        ordersData.setFees(orders.getFees());
        ordersData.setUserId(orders.getUser().getId());
        ordersData.setSuccessAmount(orders.getSuccessAmount());
        ordersData.setStatus(orders.getStatus());
        ordersData.setLeftCount(orders.getLeftCount());
        ordersData.setLeftfees(orders.getLeftfees());
        ordersData.setPrize(orders.getPrize());
        ordersData.setType(orders.getType());
        ordersData.setMarketId(orders.getMarket().getId());
        rabbitService.publish(Keys.orders_queue+orders.getMarket().getId(), JSON.toJSONString(ordersData));
    }
}
