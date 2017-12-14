package com.liu.base.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Coin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "coin")
public class Coin implements java.io.Serializable {
	public static Integer status_normal = 1;
	public static Integer status_abnormal =0;
	@Id
	@GeneratedValue
	private Integer id;
	private boolean FIsWithDraw;// 是否可以提现
	private boolean FIsRecharge;// 是否可以充值
	private String name;
	private String ShortName;
	private String description;
	private Integer status;// VirtualCoIntegerypeStatusEnum
	private String symbol;
	private String accessKey;
	private String secrtKey;
	private String ip;
	private String port;
	@Version
	private Integer version;
	private String url;
	private Double totalAmount;//总量
	private Double upDown;//日涨跌
	private Double upDownWeek;//周涨跌
	private Integer homeOrder;	//首页次序
	private Integer typeOrder; //板块次序
	private Integer totalOrder; //所有币的排序，用于充币提币
	private Integer confirmTimes;	// 充值确认次数
	private Date createTime;
	private Date updateTime;

	public Coin() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getUpDown() {
		return upDown;
	}

	public void setUpDown(Double upDown) {
		this.upDown = upDown;
	}

	public Double getUpDownWeek() {
		return upDownWeek;
	}

	public void setUpDownWeek(Double upDownWeek) {
		this.upDownWeek = upDownWeek;
	}

	public Integer getHomeOrder() {
		return homeOrder;
	}

	public void setHomeOrder(Integer homeOrder) {
		this.homeOrder = homeOrder;
	}

	public Integer getTypeOrder() {
		return typeOrder;
	}

	public void setTypeOrder(Integer typeOrder) {
		this.typeOrder = typeOrder;
	}

	public Integer getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(Integer totalOrder) {
		this.totalOrder = totalOrder;
	}

	public Integer getConfirmTimes() {
		return confirmTimes;
	}

	public void setConfirmTimes(Integer confirmTimes) {
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