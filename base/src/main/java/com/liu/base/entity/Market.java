package com.liu.base.entity;

import javax.persistence.*;
/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/12
 */
@Entity
@Table(name = "market")
public class Market {

    public static final int STATUS_Normal = 1;              // 正常
    public static final int STATUS_Abnormal = 0;            // 禁用
    public static final int TRADE_STATUS_Normal = 1;        // 正常交易
    public static final int TRADE_STATUS_Abnormal = 0;      // 禁用交易

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buy_id")
    private Coin buyCoin;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sell_id")
    private Coin sellCoin;
    private int decimals;
    private double buyFee;
    private double sellFee;
    private double minCount;
    private double maxCount;
    private double minPrice;
    private double maxPrice;
    private double minMoney;
    private double maxMoney;
    private String tradeTime;
    private int status;
    private int tradeStatus;
    private double updown;
    @Version
    private int version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coin getBuyCoin() {
        return buyCoin;
    }

    public void setBuyCoin(Coin buyCoin) {
        this.buyCoin = buyCoin;
    }

    public Coin getSellCoin() {
        return sellCoin;
    }

    public void setSellCoin(Coin sellCoin) {
        this.sellCoin = sellCoin;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public double getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(double buyFee) {
        this.buyFee = buyFee;
    }

    public double getSellFee() {
        return sellFee;
    }

    public void setSellFee(double sellFee) {
        this.sellFee = sellFee;
    }

    public double getMinCount() {
        return minCount;
    }

    public void setMinCount(double minCount) {
        this.minCount = minCount;
    }

    public double getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(double maxCount) {
        this.maxCount = maxCount;
    }

    public double getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(double minMoney) {
        this.minMoney = minMoney;
    }

    public double getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public double getUpdown() {
        return updown;
    }

    public void setUpdown(double updown) {
        this.updown = updown;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
