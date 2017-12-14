package com.liu.deal.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@Component
@org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "hello")
public class RabbitListener {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void subscribe(String message){
        System.out.println("接收的消息是"+message);
    }


}
