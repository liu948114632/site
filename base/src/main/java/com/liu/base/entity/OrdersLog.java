package com.liu.base.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/13
 */
@Entity
public class OrdersLog {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;
    @Column(columnDefinition = "decimal(20,8)", nullable = false)
    private Double amount;
    @Column(columnDefinition = "decimal(20,8)", nullable = false)
    private Double price;
    @Column(columnDefinition = "decimal(20,8)", nullable = false)
    private Double count;
    @Version
    private int version;
    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;
    private Date createTime;

    public OrdersLog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
