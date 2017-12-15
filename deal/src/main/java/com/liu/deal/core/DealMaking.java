package com.liu.deal.core;

import com.liu.deal.model.OrdersData;
import com.liu.deal.model.OrdersLogData;
import com.liu.deal.utils.EntrustTypeEnum;
import com.liu.deal.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 *撮合主要逻辑，数据库操作
 */
@Service
public class DealMaking {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private DealServiceDB dealServiceDB;

    public double deal(OrdersData _buyOrders, OrdersData _sellOrders, Integer markerId){

        double successPrice;    // 成交价
        double successCount;    // 成交量

        // 传过来的对象是缓存对象，不能直接修改，只有操作成功了才能修改，不可靠，可以在更新的sql上面加上状态检查
        OrdersData buyOrders = _buyOrders.clone();
        OrdersData sellOrders = _sellOrders.clone();

        //先入为主 成交价是先挂的单
        if (buyOrders.getId() > sellOrders.getId()) {
            successPrice = sellOrders.getPrize();
        } else {
            successPrice = buyOrders.getPrize();
        }

        if (buyOrders.getLeftCount() > sellOrders.getLeftCount()) {
            successCount = sellOrders.getLeftCount();
        } else {
            successCount = buyOrders.getLeftCount();
        }

        Timestamp updateTime = new Timestamp(new Date().getTime());
        //成交单日志
        OrdersLogData ordersLog = new OrdersLogData();
        ordersLog.setAmount(MathUtils.multiply(successCount, successPrice));
        ordersLog.setPrize(successPrice);
        ordersLog.setCount(successCount);
        ordersLog.setCreateTime(updateTime);
        ordersLog.setActive(buyOrders.getId() > sellOrders.getId());
        ordersLog.setMarketId(markerId);
        //买单id大 先挂的是卖单   成交单显示主动购买
        ordersLog.setType(buyOrders.getId()>sellOrders.getId()? EntrustTypeEnum.BUY:EntrustTypeEnum.SELL);

        //卖单日志
//        OrdersLogData sellLog = new OrdersLogData();
//        sellLog.setAmount(MathUtils.multiply(successCount, successPrice));
//        sellLog.setCount(successCount);
//        sellLog.setPrize(successPrice);
//        sellLog.setCreateTime(updateTime);
//        sellLog.setActive(sellOrders.getId() > buyOrders.getId());
//        sellLog.setMarketId(markerId);
//        sellLog.setType(EntrustTypeEnum.SELL);


        log.trace("updateDealMaking buy = {}, sell = {}, successPrice = {}, successCount = {},"+
                buyOrders.getId(), sellOrders.getId(), successPrice, successCount );

        boolean isSuccesss = dealServiceDB.dealDB(buyOrders, sellOrders, ordersLog);
        // 如果不成功，返回成交量为0
        if (!isSuccesss) {
            log.error("update unsuccessful.");
            successCount = 0D;
        } else {
//            dealMarkingListener.writeLog(buyLog);
//            dealMarkingListener.writeLog(sellLog);
            log.trace("update successful.");
            log.trace("sync entrust cache buy = {}, sell = {}", _buyOrders.getId(), _sellOrders.getId());
            // 同步缓存
            _buyOrders.setLeftCount(buyOrders.getLeftCount());
            _buyOrders.setSuccessAmount(buyOrders.getSuccessAmount());
            _buyOrders.setLeftfees(buyOrders.getLeftfees());
            _buyOrders.setStatus(buyOrders.getStatus());
            _buyOrders.setUpdatTime(buyOrders.getUpdatTime());

            _sellOrders.setLeftCount(sellOrders.getLeftCount());
            _sellOrders.setSuccessAmount(sellOrders.getSuccessAmount());
            _sellOrders.setLeftfees(sellOrders.getLeftfees());
            _sellOrders.setStatus(sellOrders.getStatus());
            _sellOrders.setUpdatTime(sellOrders.getUpdatTime());
        }
        return successCount;
    }

}
