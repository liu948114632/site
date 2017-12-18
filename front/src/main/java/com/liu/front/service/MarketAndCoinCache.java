package com.liu.front.service;

import com.liu.base.dao.CoinDao;
import com.liu.base.dao.MarketDao;
import com.liu.base.entity.Coin;
import com.liu.base.entity.Market;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@Component
public class MarketAndCoinCache {
    static Logger logger = Logger.getLogger(MarketAndCoinCache.class);

    @Autowired
    private MarketDao marketDao;

    @Autowired
    private CoinDao coinDao;

    private List<Market> markets;

    private List<Coin> coins;

    public Market getMarketById(int id){
        if(markets == null){
            return null;
        }
        for(Market market : markets){
            if(market.getId() == id){
                return market;
            }
        }
        return null;
    }

    public Coin getCoinById(int id){
        if(coins == null){
            return null;
        }
        for (Coin coin: coins){
            if(coin.getId() == id){
                return coin;
            }
        }
        return null;
    }

    public List<Coin> getCoins(){
        return this.coins;
    }

    @PostConstruct
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void syncMarkets(){
        markets = marketDao.findByStatusAndTradeStatus(Market.STATUS_Normal, Market.TRADE_STATUS_Normal);
        logger.info("同步市场成功");
    }

    @PostConstruct
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void syncCoins(){
        coins = coinDao.findByStatus(Coin.status_normal);
        logger.info("同步币种成功");
    }
}
