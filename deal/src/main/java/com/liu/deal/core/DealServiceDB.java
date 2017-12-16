package com.liu.deal.core;

import com.liu.deal.model.OrdersData;
import com.liu.deal.model.OrdersLogData;
import com.liu.deal.utils.EntrustStatusEnum;
import com.liu.deal.utils.EntrustTypeEnum;
import com.liu.deal.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 具体的撮合逻辑，操作数据库
 */
@Service
public class DealServiceDB {
    private Logger log = LoggerFactory.getLogger(getClass());

    JdbcTemplate jdbcTemplate;

    private static final String CALL_DEAL_MARKING = "call dealMarking(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    boolean dealDB(final OrdersData buy, final OrdersData sell, OrdersLogData logData)  {
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

            double buyFee = MathUtils.multiply(logData.getCount(), buyFeeRate);
            buy.setLeftCount(MathUtils.subtract(buy.getLeftCount(), logData.getCount()));
            buy.setSuccessAmount(MathUtils.add(buy.getSuccessAmount(), logData.getAmount()));
            buy.setUpdatTime(logData.getCreateTime());
            buy.setLeftfees(buy.getLeftfees() - buyFee);
            if (buy.getLeftCount() < 0.00000001F) {
                buy.setStatus(EntrustStatusEnum.AllDeal);
                buy.setLeftCount(0D);
            } else {
                buy.setStatus(EntrustStatusEnum.PartDeal);
            }

            double sellFee = logData.getCount() / sell.getCount() * sell.getFees();
            sell.setLeftCount(MathUtils.subtract(sell.getLeftCount(), logData.getCount()));
            sell.setSuccessAmount(MathUtils.add(sell.getSuccessAmount(), logData.getAmount()));
            sell.setLeftfees(sell.getLeftfees() - sellFee);
            sell.setUpdatTime(logData.getCreateTime());
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
            call.setDouble(++i, buy.getSuccessAmount());
            call.setDouble(++i, buy.getLeftCount());
            call.setInt(++i, buy.getStatus());
            call.setTimestamp(++i, buy.getUpdatTime());
            call.setDouble(++i, buyFee);                    // 买入交易手续费
            call.setInt(++i, buy.getId());

            // sell
            call.setDouble(++i, sell.getSuccessAmount());
            call.setDouble(++i, sell.getLeftCount());
            call.setInt(++i, sell.getStatus());
            call.setTimestamp(++i, sell.getUpdatTime());
            call.setDouble(++i, sell.getLeftfees());
            call.setInt(++i, sell.getId());

            // log
            call.setDouble(++i, logData.getAmount());
            call.setDouble(++i, logData.getPrize());
            call.setDouble(++i, logData.getCount());
            call.setInt(++i, logData.getType());
            call.setInt(++i, logData.getMarketId());


            // wallet
            call.setInt(++i, buy.getUserId());
            call.setInt(++i, sell.getUserId());
            call.setDouble(++i, buyLeftAmount);
            call.setDouble(++i, MathUtils.multiply(logData.getAmount(), MathUtils.subtract(1, sellFeeRate)));

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

    private double getFfee(int marketId, int type){
       return 0;
    }
}
