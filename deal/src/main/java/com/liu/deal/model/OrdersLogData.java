package com.liu.deal.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class OrdersLogData implements Serializable, Comparable {

    private int id;
    private Timestamp createTime;      // 成交时间
    private int type;           // 交易类型 0买 表示卖单先下，被购买
    private int marketId;                 // 货币ID
    private double prize;              // 成交价
    private double count;              // 成交价
    private double amount;             // 成交额
    private int buyUserId;
    private int sellUserId;
    public OrdersLogData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getBuyUserId() {
        return buyUserId;
    }

    public void setBuyUserId(int buyUserId) {
        this.buyUserId = buyUserId;
    }

    public int getSellUserId() {
        return sellUserId;
    }

    public void setSellUserId(int sellUserId) {
        this.sellUserId = sellUserId;
    }

    @Override
    public int compareTo(Object o) {
        OrdersLogData o1 = this;
        OrdersLogData o2 = (OrdersLogData) o;
        boolean flag = o1.id == o2.id && o1.id != 0 ;
        if(flag){
            return 0 ;
        }
        int ret = o2.id - o1.id;
        if (ret > 0) {
            ret = 1;
        } else if (ret < 0) {
            ret = -1;
        }
        return ret;
    }

    @Override
    public String toString() {
        return "OrdersLogData{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", type=" + type +
                ", marketId=" + marketId +
                ", prize=" + prize +
                ", count=" + count +
                ", amount=" + amount +
                ", buyUserId=" + buyUserId +
                ", sellUserId=" + sellUserId +
                '}';
    }
}
