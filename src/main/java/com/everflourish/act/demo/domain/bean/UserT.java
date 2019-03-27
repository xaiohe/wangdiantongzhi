package com.everflourish.act.demo.domain.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * demo 实体类
 * @author hzbin
 * @date 2017-6-22 上午11:09:06
 * @version 0.1.0
 */
@Entity
@Table(name ="user_t")
public class UserT {
	
	private Integer tId;
	private String tName;
	
	@Id
	@GeneratedValue
	public Integer gettId() {
		return tId;
	}
	public void settId(Integer tId) {
		this.tId = tId;
	}
	public String gettName() {
		return tName;
	}
	public void settName(String tName) {
		this.tName = tName;
	}
	@Override
	public String toString() {
		return "UserT [tId=" + tId + ", tName=" + tName + "]";
	}
}
