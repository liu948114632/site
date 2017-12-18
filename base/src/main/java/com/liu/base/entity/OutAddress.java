package com.liu.base.entity;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class OutAddress implements java.io.Serializable {

	@Id
	@GeneratedValue
	private int id;
	@Column(length = 4)
	private Integer coinId;
	private String adderess;
	private String label;   //描述
	@Column(length = 4)
	private Integer userId ;
	private Timestamp createTime;
	private boolean isDefault;  //是否默认
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

	public String getAdderess() {
		return adderess;
	}

	public void setAdderess(String adderess) {
		this.adderess = adderess;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}