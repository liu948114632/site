package com.liu.base.entity;

import javax.persistence.Entity;
import java.util.Date;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/12
 */
@Entity
public class Wallet {
    private int fid;
    private Coin coin;
    private double ftotal;
    private double ffrozen;
    private Date flastUpdateTime;
    private User user ;
    private int version ;

    private double fborrowBtc;//已借款
    private double fHaveAppointBorrowBtc ;//已预约借款

    private double fcanlendBtc;//可放款
    private double ffrozenLendBtc;//冻结放款
    private double falreadyLendBtc;//已放款
}
