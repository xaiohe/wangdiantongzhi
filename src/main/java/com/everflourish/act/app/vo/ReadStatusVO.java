package com.everflourish.act.app.vo;

import java.io.Serializable;

/**  
 * @Title:  ReadStatusVO.java   
 * @Package com.everflourish.act.app.vo   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: yangming
 * @date:   2019年3月15日 上午10:07:43   
 * @version V1.0  
 */
public class ReadStatusVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9097743582640268165L;

	private String name;
	
	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ReadStatusVO [name=" + name + ", status=" + status + "]";
	}
}
