package com.liu.base.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Coin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "coin")
public class Coin implements java.io.Serializable {
	@Id
	@GeneratedValue
	private int id;
	private boolean FIsWithDraw;// 是否可以提现
	private boolean FIsRecharge;// 是否可以充值
	private String name;
	private String ShortName;
	private String description;
	private int status;// VirtualCoinTypeStatusEnum
	private String symbol;
	private String accessKey;
	private String secrtKey;
	private String ip;
	private String port;
	@Version
	private int version;
	private String url;
	private double totalAmount;//总量
	private double upDown;//日涨跌
	private double upDownWeek;//周涨跌
	private int homeOrder;	//首页次序
	private int typeOrder; //板块次序
	private int totalOrder; //所有币的排序，用于充币提币
	private int confirmTimes;	// 充值确认次数
	private Date createTime;
	private Date updateTime;

	public Coin() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isFIsWithDraw() {
		return FIsWithDraw;
	}

	public void setFIsWithDraw(boolean FIsWithDraw) {
		this.FIsWithDraw = FIsWithDraw;
	}

	public boolean isFIsRecharge() {
		return FIsRecharge;
	}

	public void setFIsRecharge(boolean FIsRecharge) {
		this.FIsRecharge = FIsRecharge;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return ShortName;
	}

	public void setShortName(String shortName) {
		ShortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecrtKey() {
		return secrtKey;
	}

	public void setSecrtKey(String secrtKey) {
		this.secrtKey = secrtKey;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getUpDown() {
		return upDown;
	}

	public void setUpDown(double upDown) {
		this.upDown = upDown;
	}

	public double getUpDownWeek() {
		return upDownWeek;
	}

	public void setUpDownWeek(double upDownWeek) {
		this.upDownWeek = upDownWeek;
	}

	public int getHomeOrder() {
		return homeOrder;
	}

	public void setHomeOrder(int homeOrder) {
		this.homeOrder = homeOrder;
	}

	public int getTypeOrder() {
		return typeOrder;
	}

	public void setTypeOrder(int typeOrder) {
		this.typeOrder = typeOrder;
	}

	public int getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}

	public int getConfirmTimes() {
		return confirmTimes;
	}

	public void setConfirmTimes(int confirmTimes) {
		this.confirmTimes = confirmTimes;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	//通过ID判断两个币种是否为同一个币种
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		if(obj instanceof Coin){
			Coin coin = (Coin) obj;
			if(id == coin.getId()){
				return true;
			}
		}
		return false;
	}
}