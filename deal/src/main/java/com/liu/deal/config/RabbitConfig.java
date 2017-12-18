package com.liu.deal.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/18
 */
@Configuration
public class RabbitConfig {
    //新建队列   1个队列 多了也没用 true 持久化
    @Bean
    public Queue queue() {
        return new Queue("orders_queue",true);
    }

    //新建交换机， 名称为exchange。  配置交换机后，需要在rabbit的配置界面加上该exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("orders_exchange");
    }

    //配置交换机的路由方式  以orders 开头的都 接受。 绑定队列，是通过hello  来传送的
    @Bean
    Binding bindingQueue1(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("orders.*");//*表示一个词,#表示零个或多个词
    }


}
