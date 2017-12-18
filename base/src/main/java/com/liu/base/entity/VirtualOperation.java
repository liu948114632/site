package com.liu.base.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class VirtualOperation implements java.io.Serializable {

	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	@ManyToOne
	@JoinColumn(name = "coinId")
	private Coin coin;
	private Timestamp createTime;
	private Timestamp updateTime;
	private double amount;
	private double fees;
	private int feeCoinType;
	private int type;// 充值或提现VirtualCapitalOperationTypeEnum
	@Transient
	private String type_s;
	private int status;// VirtualOperationInStatusEnum||VirtualCoinOperationOutStatusEnum
	@Transient
	private String status_s;
	private String toAddress;// 提现地址
	private String fromAddress;// 充值地址
	private String tradeUniqueNumber ;
	private int confirmations ;//确认数
	private int version;
	private boolean hasOwner ;//充值记录是否归属某用户


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Coin getCoin() {
		return coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getFees() {
		return fees;
	}

	public void setFees(double fees) {
		this.fees = fees;
	}

	public int getFeeCoinType() {
		return feeCoinType;
	}

	public void setFeeCoinType(int feeCoinType) {
		this.feeCoinType = feeCoinType;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getType_s() {
		return type_s;
	}

	public void setType_s(String type_s) {
		this.type_s = type_s;
	}

	public String getStatus_s() {
		return status_s;
	}

	public void setStatus_s(String status_s) {
		this.status_s = status_s;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getTradeUniqueNumber() {
		return tradeUniqueNumber;
	}

	public void setTradeUniqueNumber(String tradeUniqueNumber) {
		this.tradeUniqueNumber = tradeUniqueNumber;
	}

	public int getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isHasOwner() {
		return hasOwner;
	}

	public void setHasOwner(boolean hasOwner) {
		this.hasOwner = hasOwner;
	}
}