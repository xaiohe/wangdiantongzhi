package com.everflourish.act.common.vo;


/**
 * 统一返回码
 * @author hzbin
 * @date 2017-6-22 上午11:09:06
 * @version 0.1.0
 */
public class ResultCode {
	/**
	 * 成功
	 */
	public static final String SUCCESS="000000I";
	/**
	 * 系统异常
	 */
	public static final String SYSTEM_ERROR="999999E";
	/**
	 * 用户不存在
	 */
	public static final String USER_NOT_FOUND="E000001";
	
	/**
	 * 参数为空
	 */
	public static final String PARAMS_IS_EMPTY="E000002";
	/**
	 * 网点未找到
	 */
	public static final String WD_NOT_FOUND="E000003";
	/**
	 * 用户已读
	 */
	public static final String USER_IS_READ="E000004";
	/**
	 * 
	 */
	public static final String ADMIN_NOT_FOUND="E000005";
}
 