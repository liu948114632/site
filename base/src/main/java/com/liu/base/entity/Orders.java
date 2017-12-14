package com.liu.base.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/12
 */
@Entity
public class Orders {
    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    @JoinColumn(name = "market")
    private Market market;
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    private Date createTime;
    private Date lastUpdatTime;
    @Column(length = 4)
    private Integer type;// 0 买 1 卖
    @Column(columnDefinition = "decimal(20,8)")
    private Double prize;
    @Column(columnDefinition = "decimal(20,8)")
    private Double amount;
    @Column(columnDefinition = "decimal(20,8)")
    private Double fees ;
    @Column(columnDefinition = "decimal(20,8)")
    private Double leftfees ;
    @Column(columnDefinition = "decimal(20,8)")
    private Double successAmount;
    @Column(columnDefinition = "decimal(20,8)")
    private Double count;
    @Column(columnDefinition = "decimal(20,8)")
    private Double leftCount;// 未成交数量
    private Integer status;// 1 未成交 2 部分成交 3 完全成交 4 已取消
    @Transient
    private String status_s;
    private boolean isLimit;// 按照市价完全成交的订单
    @Version
    private Integer version;
    private boolean hasSubscription;
    private Integer robotStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdatTime() {
        return lastUpdatTime;
    }

    public void setLastUpdatTime(Date lastUpdatTime) {
        this.lastUpdatTime = lastUpdatTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getPrize() {
        return prize;
    }

    public void setPrize(Double prize) {
        this.prize = prize;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public Double getLeftfees() {
        return leftfees;
    }

    public void setLeftfees(Double leftfees) {
        this.leftfees = leftfees;
    }

    public Double getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(Double successAmount) {
        this.successAmount = successAmount;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(Double leftCount) {
        this.leftCount = leftCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isLimit() {
        return isLimit;
    }

    public void setLimit(boolean limit) {
        isLimit = limit;
    }

    public boolean isHasSubscription() {
        return hasSubscription;
    }

    public void setHasSubscription(boolean hasSubscription) {
        this.hasSubscription = hasSubscription;
    }

    public Integer getRobotStatus() {
        return robotStatus;
    }

    public void setRobotStatus(Integer robotStatus) {
        this.robotStatus = robotStatus;
    }

    public String getStatus_s() {
        if(status == 1){
         return "未成交";
        }
        if(status == 2){
            return "部分成交";
        }
        if(status == 3){
            return "完全成交";
        }else {
            return "已取消";
        }
    }

}
