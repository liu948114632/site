package com.liu.deal.service;

import com.liu.deal.core.TradingSystem;
import com.liu.deal.model.OrdersData;
import com.liu.deal.utils.EntrustStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@Component
public class MyRabbitListener {
    private final Logger log = LoggerFactory.getLogger(RabbitListener.class);

    @Autowired
    ExecutorService executorService ;

    //处理撮合的map，里面有多个TradingSystem的实例，  每一个币种一个线程
    private Map<Integer, TradingSystem> systemMap = new ConcurrentHashMap<>();

    private final Set<String> messageSet = new ConcurrentSkipListSet<>();

    @Autowired
    RabbitTemplate rabbitTemplate;

    //配置多个接收消息的类。可以并发处理
    @RabbitListener(queues = "orders_queue")
    public  void  sub(OrdersData ordersData){
        subscribe(ordersData);
        log.info("我是sub接收的："+ordersData);
    }
    @RabbitListener(queues = "orders_queue")
    public  void  sub1(OrdersData ordersData){
        subscribe(ordersData);
        log.info("我是sub1接收的："+ordersData);
    }

    private void subscribe(OrdersData message){
        // 给每个消息一个ID，避免在启动过程中，从数据库加载的一遍，消息队列又来一遍，导致消息重复的问题
        // 从消息队列过来的消息只有两种，一种是创建，一种是取消，
        // 有一种极端情况：当撮合引擎停掉了，或者在重启，这个时候用户在前台下单，消息已经进队列
        // 这个时候撮合引擎启动，会把数据库中的所有订单加到内存中，可能就包含了刚才用户下的那条订单
        // 当撮合启动成功，并开始监听消息队列的时候，刚才那条订单就会传递到这里，这个时候就会出现重复下单的消息
        // 不能简单的根据ID来过滤掉，因为用户下单之后，又取消了，消息队列里面还有有一条取消的消息
        // 所有通过订单ID + 状态来给消息编号，过滤掉重复的情况
        // 只有在重启的时候，下单才会出现这样的情况
        String messageId = message.getId() + "_" + message.getStatus();
        synchronized (messageSet) {
            if (messageSet.contains(messageId)) {
                return;
            }
            messageSet.add(messageId);
        }

        log.debug("onMessage {}", message);
        TradingSystem tradingSystem = systemMap.get(message.getMarketId());
        if (tradingSystem == null) {
            log.debug("new " + message.getMarketId() + " system");
            //该币种的撮合系统不存在，新建一个
            tradingSystem = new TradingSystem();
            systemMap.put(message.getMarketId(), tradingSystem);
            executorService.submit(tradingSystem);
//            new Thread(tradingSystem).start();
        }
        // 收到的订单，如果是取消状态，则取消
        if (message.getStatus() == EntrustStatusEnum.Cancel) {
            // 极端情况下，用户下了单，但是没有被撮合，这个时候撮合引擎重启了，然后用户又取消了
            // 导致队列里面有有取消的消息，撮合引擎重启的时候，又把这条当成已取消了
            // 如果直接更新深度挂单数据，则会出现多减的情况
            if (tradingSystem.getOrders(message.getId()) != null) {
//                entrustListener.onCreateEntrust(message.clone());    //监听事件，就是下单，取消订单 以后通知redis，redis发布，推给push到前端
            }
            tradingSystem.cancelOrders(message);
            return;
        }
//        entrustListener.onCreateEntrust(message.clone());
//        entrustListener.onCreateEntrust(message);
        tradingSystem.addOrsersIntoQueue(message);
    }


}
