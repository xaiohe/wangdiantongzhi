package com.everflourish.act.app.vo;

import java.io.Serializable;
import java.util.List;

import com.everflourish.act.app.domain.bean.NoticeRecord;

/**  
 * @Title:  Notic.java   
 * @Package com.everflourish.act.app.vo   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: yangming
 * @date:   2019年3月15日 上午10:03:34   
 * @version V1.0  
 */
public class NoticeRecordVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6586349924205916968L;
	
	private NoticeRecord nr;
	
	private List<ReadStatusVO> rsList;

	public NoticeRecord getNr() {
		return nr;
	}

	public void setNr(NoticeRecord nr) {
		this.nr = nr;
	}

	public List<ReadStatusVO> getRsList() {
		return rsList;
	}

	public void setRsList(List<ReadStatusVO> rsList) {
		this.rsList = rsList;
	}

	@Override
	public String toString() {
		return "NoticeRecordVO [nr=" + nr + ", rsList=" + rsList + "]";
	}
}
