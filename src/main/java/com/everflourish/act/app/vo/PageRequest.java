package com.everflourish.act.app.vo;

import java.io.Serializable;

/**  
 * @Title:  PageRequest.java   
 * @Package com.everflourish.act.app.vo   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: yangming
 * @date:   2019年3月15日 上午10:41:44   
 * @version V1.0  
 */
public class PageRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8248320701905210017L;
	
	private Integer page;

	private Integer pageSize;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		if(page == null) {
			page = 1;
		}
		this.page = page;
	}

	public Integer getPageSize() {
		if(pageSize == null) {
			pageSize = 20;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
