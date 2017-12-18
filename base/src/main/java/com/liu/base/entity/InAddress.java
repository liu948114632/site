package com.liu.base.entity;


import javax.persistence.*;
import java.sql.Timestamp;
@Entity
public class InAddress implements java.io.Serializable {
	@Id
	@GeneratedValue
	private int id;
	private Integer coinId;
	private String address;
	private Integer userId ;
	private Timestamp createTime;
	@Version
	private int version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCoinId() {
		return coinId;
	}

	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}

	public String getAddress() {
		return address;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public void setAddress(String adderess) {
		this.address = adderess;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}