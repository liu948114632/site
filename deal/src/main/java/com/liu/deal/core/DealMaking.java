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

    public double deal(OrdersData _buyFentrust, OrdersData _sellFentrust, Integer markerId){

        double successPrice;    // 成交价
        double successCount;    // 成交量

        // 传过来的对象是缓存对象，不能直接修改，只有操作成功了才能修改，不可靠，可以在更新的sql上面加上状态检查
        OrdersData buyFentrust = _buyFentrust.clone();
        OrdersData sellFentrust = _sellFentrust.clone();

        //先入为主 成交价是先挂的单
        if (buyFentrust.getId() > sellFentrust.getId()) {
            successPrice = sellFentrust.getPrize();
        } else {
            successPrice = buyFentrust.getPrize();
        }

        if (buyFentrust.getLeftCount() > sellFentrust.getLeftCount()) {
            successCount = sellFentrust.getLeftCount();
        } else {
            successCount = buyFentrust.getLeftCount();
        }

        Timestamp updateTime = new Timestamp(new Date().getTime());
        //买单日志
        OrdersLogData buyLog = new OrdersLogData();
        buyLog.setAmount(MathUtils.multiply(successCount, successPrice));
        buyLog.setPrize(successPrice);
        buyLog.setCount(successCount);
        buyLog.setCreateTime(updateTime);
        buyLog.setActive(buyFentrust.getId() > sellFentrust.getId());
        buyLog.setMarketId(markerId);
        buyLog.setType(EntrustTypeEnum.BUY);

        //卖单日志
        OrdersLogData sellLog = new OrdersLogData();
        sellLog.setAmount(MathUtils.multiply(successCount, successPrice));
        sellLog.setCount(successCount);
        sellLog.setPrize(successPrice);
        sellLog.setCreateTime(updateTime);
        sellLog.setActive(sellFentrust.getId() > buyFentrust.getId());
        sellLog.setMarketId(markerId);
        sellLog.setType(EntrustTypeEnum.SELL);

        log.trace("updateDealMaking buy = {}, sell = {}, successPrice = {}, successCount = {}, buy active = {}, sell active = {}",
                buyFentrust.getId(), sellFentrust.getId(), successPrice, successCount, buyLog.isActive(), sellLog.isActive());

        boolean isSuccesss = dealServiceDB.dealDB(buyFentrust, sellFentrust, buyLog, sellLog);
        // 如果不成功，返回成交量为0
        if (!isSuccesss) {
            log.error("update unsuccessful.");
            successCount = 0D;
        } else {
//            dealMarkingListener.writeLog(buyLog);
//            dealMarkingListener.writeLog(sellLog);
            log.trace("update successful.");
            log.trace("sync entrust cache buy = {}, sell = {}", _buyFentrust.getId(), _sellFentrust.getId());
            // 同步缓存
            _buyFentrust.setLeftCount(buyFentrust.getLeftCount());
            _buyFentrust.setSuccessAmount(buyFentrust.getSuccessAmount());
            _buyFentrust.setLeftfees(buyFentrust.getLeftfees());
            _buyFentrust.setStatus(buyFentrust.getStatus());
            _buyFentrust.setUpdatTime(buyFentrust.getUpdatTime());

            _sellFentrust.setLeftCount(sellFentrust.getLeftCount());
            _sellFentrust.setSuccessAmount(sellFentrust.getSuccessAmount());
            _sellFentrust.setLeftfees(sellFentrust.getLeftfees());
            _sellFentrust.setStatus(sellFentrust.getStatus());
            _sellFentrust.setUpdatTime(sellFentrust.getUpdatTime());
        }
        return successCount;
    }

}
