package com.everflourish.act.common.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 通用返回VO（值模型）
 * @author hzbin
 * @date 2017-6-22 上午11:09:06
 * @version 0.1.0
 */
@JsonInclude(value=Include.NON_NULL)
public class CommonResultVO<T> implements Serializable {

	private static final long serialVersionUID = -3469987287745558998L;

	private String resultCode;
	private String errmsg;
	private transient T resultData;
	public String getResultCode() {
		if(resultCode == null){
			resultCode = ResultCode.SUCCESS;
		}
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public T getResultData() {
		return resultData;
	}
	public void setResultData(T resultData) {
		this.resultData = resultData;
	}
	@Override
	public String toString() {
		return "CommonResultVo [resultCode=" + resultCode + ", errmsg=" + errmsg + ", resultData=" + resultData + "]";
	}
	public CommonResultVO(String resultCode, String errmsg, T resultData) {
		super();
		this.resultCode = resultCode;
		this.errmsg = errmsg;
		this.resultData = resultData;
	}
	public CommonResultVO(String resultCode, String errmsg) {
		super();
		this.resultCode = resultCode;
		this.errmsg = errmsg;
	}
	public CommonResultVO(String resultCode, T resultData) {
		super();
		this.resultCode = resultCode;
		this.resultData = resultData;
	}
	public CommonResultVO(T resultData) {
		super();
		this.resultCode = ResultCode.SUCCESS;
		this.resultData = resultData;
	}
	public CommonResultVO() {
		super();
	}
	
}
