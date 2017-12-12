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
public class Order implements Serializable{

    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    @JoinColumn(name = "market")
    private Market market;
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    private Date createTime;
    private Date lastUpdatTime;
    private int type;// 0 买 1 卖
    private double prize;
    private double amount;
    private double fees ;
    private double leftfees ;
    private double successAmount;
    private double count;
    private double leftCount;// 未成交数量
    private int status;// EntrustStatusEnum
    private boolean fisLimit;// 按照市价完全成交的订单
    private int version;
    private boolean fhasSubscription;
    private int robotStatus;
}
