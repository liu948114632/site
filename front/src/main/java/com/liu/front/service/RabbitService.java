package com.liu.front.service;

import com.liu.front.utils.Keys;
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
public class RabbitService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publish(String route, String msg){
        rabbitTemplate.convertAndSend(Keys.queue_exchange,route, msg);
    }
}
