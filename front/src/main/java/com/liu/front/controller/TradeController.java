package com.liu.front.controller;

import com.liu.base.entity.Market;
import com.liu.base.entity.Orders;
import com.liu.base.entity.User;
import com.liu.front.service.MarketCache;
import com.liu.front.service.RabbitService;
import com.liu.front.service.SendRabbitMessage;
import com.liu.front.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@RestController
@RequestMapping("/trade")
public class TradeController extends BaseController{

    @Autowired
    SendRabbitMessage sendRabbitMessage;

    @Autowired
    private MarketCache marketCache;

    @Autowired
    private TradeService tradeService;

    @Autowired
    RabbitService rabbitService;

    @GetMapping("/send")
    public void send(){
        rabbitService.publish1("orders.queue","你好");
        rabbitService.publish1("orders.queue1","1312313");
    }

    @PostMapping("/buySubmit")
    public Object buySubmit(int marketId, double count, double price, String tradePassword){
        User user = getUser();
        Market market = marketCache.getMarketById(marketId);
        Map result = checkCanTrade(user,tradePassword,market,count,price);
        if(result!=null){
            return result;
        }
        try {
            Orders orders = tradeService.buySubmit(user, market, count,price);
            sendRabbitMessage.sendOrdersToRabbit(orders);
            return result(200, "ok",null);
        }catch (Exception e){
            e.printStackTrace();
            return result(9,"用户余额不足",null);
        }
    }

    @PostMapping("/sellSubmit")
    public Object sellSubmit(int marketId, double count, double price, String tradePassword){
        User user = getUser();
        Market market = marketCache.getMarketById(marketId);
        Map result = checkCanTrade(user,tradePassword,market,count,price);
        if(result!=null){
            return result;
        }
        try {
            Orders orders = tradeService.sellSubmit(user, market, count,price);
            sendRabbitMessage.sendOrdersToRabbit(orders);
            return result(200, "ok",null);
        }catch (Exception e){
            e.printStackTrace();
            return result(9,"用户余额不足",null);
        }
    }

    private Map checkCanTrade(User user, String password , Market market, double count ,double price){
        if(user == null){
            return result(1,"未登陆",null);
        }

        if(!user.getTradePassword().equals(password)){
            return result(2,"交易密码不正确",null);
        }

        if(market == null){
            return result(3,"市场不存在",null);
        }
        if(count<market.getMinCount()){
            return result(4,"小于最小交易数量",null);
        }

        if(count>market.getMaxCount()){
            return result(5,"大于最大交易数量",null);
        }
        if(market.getMaxMoney()< count* price){
            return result(6,"大于最大交易e",null);
        }
        if(market.getMinMoney()> count * price){
            return result(7,"小于最小交易额",null);
        }
        //是否开放交易
        if (tradeService.openTrade(market.getTradeTime()) == false) {
            return result(8,"不在交易区间",null);
        }
        return null;
    }
}
