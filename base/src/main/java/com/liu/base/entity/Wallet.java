package com.liu.base.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : liuyuanbo
 * Date： 2017/12/12
 */
@Entity
@Table
public class Wallet {
    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    @JoinColumn(name = "coin_id")
    private Coin coin;
    @Column(columnDefinition = "decimal(20,8)")
    private Double total;
    @Column(columnDefinition = "decimal(20,8)")
    private Double frozen;
    private Date updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user ;
    @Version
    private Integer version ;

    public Wallet() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getFrozen() {
        return frozen;
    }

    public void setFrozen(double frozen) {
        this.frozen = frozen;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
