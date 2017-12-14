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

    public static final Integer STATUS_Normal = 1;              // 正常
    public static final Integer STATUS_Abnormal = 0;            // 禁用
    public static final Integer TRADE_STATUS_Normal = 1;        // 正常交易
    public static final Integer TRADE_STATUS_Abnormal = 0;      // 禁用交易

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "main_id")
    private Coin mainCoin;   //当做钱的币种，中间币
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trade_id")
    private Coin tradeCoin;   //交易的币种
    private Integer decimals;
    private Double buyFee;
    private Double sellFee;
    private Double minCount;
    private Double maxCount;
    private Double minPrice;
    private Double maxPrice;
    private Double minMoney;
    private Double maxMoney;
    private String tradeTime;
    private Integer status;
    private Integer tradeStatus;
    private Double updown;
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Coin getMainCoin() {
        return mainCoin;
    }

    public void setMainCoin(Coin mainCoin) {
        this.mainCoin = mainCoin;
    }

    public Coin getTradeCoin() {
        return tradeCoin;
    }

    public void setTradeCoin(Coin tradeCoin) {
        this.tradeCoin = tradeCoin;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Double getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(Double buyFee) {
        this.buyFee = buyFee;
    }

    public Double getSellFee() {
        return sellFee;
    }

    public void setSellFee(Double sellFee) {
        this.sellFee = sellFee;
    }

    public Double getMinCount() {
        return minCount;
    }

    public void setMinCount(Double minCount) {
        this.minCount = minCount;
    }

    public Double getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Double maxCount) {
        this.maxCount = maxCount;
    }

    public Double getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(Double minMoney) {
        this.minMoney = minMoney;
    }

    public Double getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(Double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Double getUpdown() {
        return updown;
    }

    public void setUpdown(Double updown) {
        this.updown = updown;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
