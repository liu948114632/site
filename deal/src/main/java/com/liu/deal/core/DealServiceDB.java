package com.liu.deal.core;

import com.liu.deal.model.OrdersData;
import com.liu.deal.model.OrdersLogData;
import com.liu.deal.utils.EntrustStatusEnum;
import com.liu.deal.utils.EntrustTypeEnum;
import com.liu.deal.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 具体的撮合逻辑，操作数据库
 */
@Service
public class DealServiceDB {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String CALL_DEAL_MARKING = "call dealMarking(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    boolean dealDB(final OrdersData buy, final OrdersData sell, OrdersLogData ordersLogData)  {
        Connection connection = null;
        PreparedStatement call = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            call = connection.prepareStatement(CALL_DEAL_MARKING);
            // 状态检查，如果买单，或者买单，已经取消，或者已经完全成交，则不能进行撮合更新动作
            if (buy.getStatus() == EntrustStatusEnum.Cancel
                    || buy.getStatus() == EntrustStatusEnum.AllDeal
                    || sell.getStatus() == EntrustStatusEnum.Cancel
                    || sell.getStatus() == EntrustStatusEnum.AllDeal) {
                return false;
            }

            double sellFeeRate = 0d;
            double buyFeeRate = 0d;
            sellFeeRate = getFfee(sell.getMarketId(), EntrustTypeEnum.SELL);
            buyFeeRate = getFfee(buy.getMarketId(), EntrustTypeEnum.BUY);

            log.trace("手续费", sellFeeRate);

            double buyLeftAmount = 0D;

            double buyFee = MathUtils.multiply(ordersLogData.getCount(), buyFeeRate);
            buy.setLeftCount(MathUtils.subtract(buy.getLeftCount(), ordersLogData.getCount()));
            buy.setSuccessAmount(MathUtils.add(buy.getSuccessAmount(), ordersLogData.getAmount()));
            buy.setUpdateTime(ordersLogData.getCreateTime());
            buy.setLeftfees(buy.getLeftfees() - buyFee);
            if (buy.getLeftCount() < 0.00000001F) {
                buy.setStatus(EntrustStatusEnum.AllDeal);
                buy.setLeftCount(0D);
            } else {
                buy.setStatus(EntrustStatusEnum.PartDeal);
            }

            double sellFee = ordersLogData.getCount() / sell.getCount() * sell.getFees();
            sell.setLeftCount(MathUtils.subtract(sell.getLeftCount(), ordersLogData.getCount()));
            sell.setSuccessAmount(MathUtils.add(sell.getSuccessAmount(), ordersLogData.getAmount()));
            sell.setLeftfees(sell.getLeftfees() - sellFee);
            sell.setUpdateTime(ordersLogData.getCreateTime());
            if (sell.getLeftCount() < 0.00000001F) {
                sell.setStatus(EntrustStatusEnum.AllDeal);
                sell.setLeftCount(0D);
            } else {
                sell.setStatus(EntrustStatusEnum.PartDeal);
            }

            if (buy.getStatus() == EntrustStatusEnum.AllDeal) {
                buyLeftAmount = MathUtils.subtract(buy.getAmount(), buy.getSuccessAmount());
            }

            int i = 0;
            // buy
            call.setDouble(++i, buy.getSuccessAmount());  //1
            call.setDouble(++i, buy.getLeftCount());   //2
            call.setInt(++i, buy.getStatus());  //3
            call.setTimestamp(++i, buy.getUpdateTime());  //4
            call.setDouble(++i, buyFee);                    // 买入交易手续费  5
            call.setInt(++i, buy.getId());  //6

            // sell
            call.setDouble(++i, sell.getSuccessAmount()); //7
            call.setDouble(++i, sell.getLeftCount());  //8
            call.setInt(++i, sell.getStatus());   //9
            call.setTimestamp(++i, sell.getUpdateTime());  //10
            call.setDouble(++i, sell.getLeftfees());  //11
            call.setInt(++i, sell.getId());  //12

            // log
            call.setDouble(++i, ordersLogData.getAmount());  //13
            call.setDouble(++i, ordersLogData.getPrize());  //14
            call.setDouble(++i, ordersLogData.getCount());  //15
            call.setInt(++i, ordersLogData.getType());  //16
            call.setInt(++i, ordersLogData.getMarketId());  //17

            // wallet
            call.setInt(++i, buy.getUserId()); //18
            call.setInt(++i, sell.getUserId());  //19
            call.setDouble(++i, buyLeftAmount);  //20
            call.setDouble(++i, MathUtils.multiply(ordersLogData.getAmount(), MathUtils.subtract(1, sellFeeRate))); //21

            ResultSet rs = call.executeQuery();
            int[] ret = new int[7];
            if (rs.next()) {
                ret[0] = rs.getInt(1);
                ret[1] = rs.getInt(2);
                ret[2] = rs.getInt(3);
                ret[3] = rs.getInt(4);
                ret[4] = rs.getInt(5);
                ret[5] = rs.getInt(6);
                ret[6] = rs.getInt(7);
            }

            if (ret[0] == 1
                    && ret[1] == 1
                    && ret[2] == 2
                    && ret[3] == 1
                    && ret[4] == 1
                    && ret[5] == 1
                    && ret[6] == 1) {
                log.trace("deal success buy " + buy.getId() + ", sell " + sell.getId());
                return true;
            } else {
                log.error("deal fails buy {}, sell {} updated: {}, {}, {}, {}, {}, {}, {}",
                        buy.getId(), sell.getId(), ret[0], ret[1], ret[2], ret[3], ret[4], ret[5], ret[6]);
                return false;
            }
        }catch (Exception e){
            throw new RuntimeException("match fails " + e);
        }finally {
            if(null != connection){
                try {
                    connection.close();
                    if(null !=call){
                        call.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //缓存 手续费
    private HashMap<Integer, Map> allFees = new HashMap<>();

    @PostConstruct
    @Scheduled(cron = "0 0/5 * * * ?")
    public void syncFee(){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select id, buy_fee, sell_fee FROM market");
        for (Map map : list){
            allFees.put(Integer.valueOf(map.get("id").toString()), map);
        }
    }

    private double getFfee(int marketId, int type){
        Map map = allFees.get(marketId);
        double rate = 0d;
        if(type == EntrustTypeEnum.BUY){
            Object buy_fee= map.get("buy_fee");
            if(buy_fee != null){
                rate = Double.valueOf(buy_fee.toString());
            }
        }else {
            Object sell_fee= map.get("sell_fee");
            if(sell_fee != null){
                rate = Double.valueOf(sell_fee.toString());
            }
        }
        return rate;
    }
}
