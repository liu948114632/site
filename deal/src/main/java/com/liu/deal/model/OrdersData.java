package com.liu.deal.model;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class OrdersData implements Serializable, Comparable, Cloneable ,RowMapper<OrdersData>{

	private Integer userId;
    private Integer id;            // 委托单ID
	private Double leftCount;  	// 未成交数量
	private double amount;
	private Double prize;      	// 成交价格
    private Integer marketId;         // 市场id
    private Integer type;   // 0买 2 卖
    private Integer deep;			//深度
	private Integer status;
	private Double successAmount;
	private Double count;
	private Timestamp createTime;
	private Timestamp updateTime;
	private Double fees ;
	private Double leftfees;
	private Integer walletId;			// 钱包ID

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(Double leftCount) {
		this.leftCount = leftCount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Double getPrize() {
		return prize;
	}

	public void setPrize(Double prize) {
		this.prize = prize;
	}

	public Integer getMarketId() {
		return marketId;
	}

	public void setMarketId(Integer marketId) {
		this.marketId = marketId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDeep() {
		return deep;
	}

	public void setDeep(Integer deep) {
		this.deep = deep;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
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

	public Integer getWalletId() {
		return walletId;
	}

	public void setWalletId(Integer walletId) {
		this.walletId = walletId;
	}
	@Override
	public OrdersData clone() {
		try {
			return (OrdersData) super.clone();
		} catch (Exception e) {
			return new OrdersData();
		}
	}

	@Override
    public int compareTo(Object o) {
        OrdersData o1 = this;
        OrdersData o2 = (OrdersData) o;
        boolean flag = o1.id == o2.id && o1.id != 0 ;
        if(flag){
            return 0 ;
        }
        int ret = (int) (o1.prize - o2.prize);
        if(ret == 0){
            ret = o1.id - o2.id;
        }
        if (ret > 0) {
            ret = 1;
        } else if (ret < 0) {
            ret = -1;
        }
        return ret;
    }

	@Override
	public OrdersData mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrdersData ordersData = new OrdersData();
		ordersData.setAmount(rs.getDouble("amount"));
		ordersData.setPrize(rs.getDouble("prize"));
		ordersData.setLeftCount(rs.getDouble("left_count"));
		return ordersData;
	}
}
