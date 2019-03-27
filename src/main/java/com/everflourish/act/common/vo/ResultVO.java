package com.everflourish.act.common.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 通用返回类
 * @author hzb
 * @date 2018年5月15日 上午9:55:29
 * @version 0.1.0
 */
public class ResultVO<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean success; 
	@JsonInclude(value = Include.NON_NULL)
	private String resultCode; 
	@JsonInclude(value = Include.NON_NULL)
	private String errorMessage; 

	@JsonInclude(value = Include.NON_NULL)
	private T data;
	
	@JsonInclude(value = Include.NON_NULL)
	private String sessionId;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ResultVO() {
		super();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ResultVO(boolean success) {
		super();
		this.success = success;
	}
	public ResultVO(boolean success, String resultCode,String sessionId) {
		super();
		this.success = success;
		this.resultCode = resultCode;
		this.sessionId = sessionId;
	}
	public ResultVO(boolean success, T data) {
		super();
		this.success = success;
		this.data = data;
	}

	public ResultVO(boolean success, String resultCode, String errorMessage, T data) {
		super();
		this.success = success;
		this.resultCode = resultCode;
		this.errorMessage = errorMessage;
		this.data = data;
	}
	public ResultVO(boolean success, String resultCode, String errorMessage, T data, String sessionId) {
		super();
		this.success = success;
		this.resultCode = resultCode;
		this.errorMessage = errorMessage;
		this.data = data;
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "ResultVO [success=" + success + ", resultCode=" + resultCode + ", errorMessage=" + errorMessage
				+ ", data=" + data + "]";
	}

}
