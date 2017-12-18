package com.liu.deal.core;

import com.liu.deal.model.OrdersData;
import com.liu.deal.service.MyRabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * init 方法，启动执行，
 * 1 将数据库中的挂单表放入到消息队列，重新撮合。因为可能撮合关闭，但是还有新的挂单进来。这时要重新撮合
 * 2 清空数据库中depth
 * 3 将成交日志放入redis
 */
@Component
public class Init {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    MyRabbitListener rabbitListener;

    @PostConstruct
    public void InitData(){
        //获取数据库中所有未成交记录
        List<OrdersData> list = loadOrders();
        for(OrdersData ordersData: list){
            rabbitListener.sub(ordersData);
        }
        log.info("初始化挂单表成功");
    }

    private List<OrdersData> loadOrders() {
        return jdbcTemplate.query(" SELECT *  from orders where status = 1 or status = 2",new BeanPropertyRowMapper<>(OrdersData.class));
    }


}
