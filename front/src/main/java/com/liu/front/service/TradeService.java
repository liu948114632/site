package com.liu.front.service;

import com.liu.base.dao.OrderDao;
import com.liu.base.dao.WalletDao;
import com.liu.base.entity.Market;
import com.liu.base.entity.Orders;
import com.liu.base.entity.User;
import com.liu.base.utils.MathUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@Service
public class TradeService {
    static Logger logger = Logger.getLogger(MarketCache.class);
    @Autowired
    WalletDao walletDao;

    @Autowired
    OrderDao orderDao;

    @Transactional
    public Orders buySubmit(User user, Market market, double count, double price){
        double total = MathUtils.multiply(count, price);
        double fee = MathUtils.multiply(market.getBuyFee(),total);
        //得到什么扣什么的手续费，此时不需要扣除手续费，只需要记录手续费多少就行
        int result = walletDao.buySubmit(total,user.getId(), market.getMainCoin().getId());
        if(result!= 1){
            throw new RuntimeException();
        }
        Orders orders = new Orders();
        orders.setAmount(total);
        orders.setType(0);
        orders.setCount(MathUtils.convert(count, 8));
        orders.setPrize(MathUtils.convert(price, 8));
        orders.setLeftCount(MathUtils.convert(count, 8));
        orders.setCreateTime(new Date());
        orders.setFees(fee);
        orders.setLeftCount(fee);
        orders.setMarket(market);
        orders.setStatus(1);
        orders.setSuccessAmount(0D);
        orders.setUser(user);
        orderDao.save(orders);
        return orders;
    }

    @Transactional
    public Orders sellSubmit(User user, Market market, double count, double price) {
        double total = MathUtils.multiply(count, price);
        double fee = MathUtils.multiply(market.getBuyFee(),total);
        //出售获取，出售个数是count,钱包中减少count
        int result = walletDao.sellSubmit(count,user.getId(), market.getTradeCoin().getId());
        if(result!= 1){
            throw new RuntimeException();
        }
        Orders orders = new Orders();
        orders.setType(1);
        orders.setAmount(total);
        orders.setCount(MathUtils.convert(count, 8));
        orders.setPrize(MathUtils.convert(price, 8));
        orders.setLeftCount(MathUtils.convert(count, 8));
        orders.setCreateTime(new Date());
        orders.setFees(fee);
        orders.setLeftCount(fee);
        orders.setMarket(market);
        orders.setStatus(1);
        orders.setSuccessAmount(0D);
        orders.setUser(user);
        orderDao.save(orders);
        return orders;
    }

    public boolean openTrade(String value){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        boolean isOpenTrade = false;
        try {
            String[] times = value.split("\n");
            for (String t : times) {
                //Date类befor和after方法判断方式是大于或者小于，所以在本来区间上向左向右各增加1秒
                Date beforeDate = df.parse(t.trim().split("-")[0]);
                beforeDate.setTime(beforeDate.getTime() - 1000);
                Date afterDate = df.parse(t.trim().split("-")[1]);
                afterDate.setTime(afterDate.getTime() + 1000);
                Date time = df.parse(df.format(new Date()));
                if ( time.after(beforeDate) && time.before(afterDate)) {
                    isOpenTrade = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("输入的区间格式错误！");
        }
        //抛出异常，返回false
        return isOpenTrade;
    }


}
