package com.everflourish.act.common.enums;

/**
 * @author hzb
 * @date 2018年5月15日 上午10:29:44
 * @version 0.1.0
 */
public enum ResultCodeEnum {

	/**
	 * 000000I_成功
	 */
	SUCCESS("000000I", "成功"),
	/**
	 * "999999E", "系统异常"
	 */
	ERROR("999999E", "系统异常"),
	/**
	 * 接口已过期
	 */
	DEPRECATED("999999D", "接口已过期"),
	/**
	 * 学生答案列表为空
	 */
	STU_QUE_EMPTY("E00001", "学生答案列表为空"),
	/**
	 * 参数传入异常
	 */
	PARAM_ERROR("E80001", "参数传入异常"),
	/**
	 * 请求类型错误,spring 415
	 */
	MEDIA_TYPE_ERROR("E90001", "请求类型错误,spring 415"),
	/**
	 * "100024E", "图片验证码错误"
	 */
	IMG_VALID_CODE_ERROR("100024E", "图片验证码错误"),
	/**
	 * "100007E", "短信验证码错误"
	 */
	MSG_VALID_CODE_ERROR("100007E", "短信验证码错误"),
	/**
	 * "100025E", "短信验证码不存在"
	 */
	VALID_CODE_NOT_FOUND("100025E", "短信验证码不存在"),
	/**
	 * "100020E", "验证码超时"
	 */
	VALID_CODE_TIMEOUT("100020E", "验证码超时"),
	/**
	 * "100022E", "发送验证短信频繁"
	 */
	FREQUENT_OPERATION("100022E", "发送验证短信频繁"),
	/**
	 * "100021E", "发送短信失败"
	 */
	SEND_SMS_FAILURE("100021E", "发送短信失败"),
	/**
	 * "000001E", "参数为空"
	 */
	EMPTY_INPUT("000001E", "参数为空"),
	/**
	 * "100012E","用户不存在"
	 */
	USERNAME_NOT_FOUND("100012E","用户不存在"),
	/**
	 * "100014E","密码错误"
	 */
	PASSWORD_INVALID("100014E","密码错误"),
	/**
	 * "100017E","手机号码格式错误"
	 */
	INVALID_PHONE_NO_FORMAT("100017E", "手机号码格式错误"),
	/**
	 * "100005E", "该手机号码已被注册"
	 */
	PHONE_HAS_REGISTED("100005E", "该手机号码已被注册"),
	/**
	 * "000012E","权限错误"
	 */
	ACCESS_DENIED("000012E","权限错误"),
	/**
	 * "000015E","用户不存在"
	 */
	USER_NOT_EXIST("000015E", "用户不存在"),
	/**
	 * "100008E", "用户名密码错误"
	 */
	USERNAME_PASSWORD_INVALID("100008E", "用户名密码错误"),
	/**
	 * "100009E","用户未激活"
	 */
	USER_NOT_ACTIVATED("100009E", "用户未激活"),
	/**
	 * "100010E","用户已锁定"
	 */
	USER_FORBIDDEN("100010E", "用户已锁定"),
	/**
	 * "100026E","图片验证码不存在"
	 */
	IMG_CODE_NOT_FOUND("100026E", "图片验证码不存在"),
	/**
	 * "100027E","没有设置密码的权限"
	 */
	SET_PASSWD_ERROR("100027E", "没有设置密码的权限"),
	/**
	 * "100028E","设置密码失败"
	 */
	SET_PASSWD_FILD("100028E", "设置密码失败"),
	/**
	 * "100029E", "ip限制"
	 */
	IP_LIMIT("100029E", "ip限制"),
	/**
	 * "100030E", "设置密码超时"
	 */
	SET_PASSWD_TIMEOUT("100030E", "设置密码超时"),
	/**
	 * "200016E","学生人数超限制"
	 */
	STUDENT_COUNT_EXCEED("200016E","学生人数超限制"),
	/**
	 * "200028E","共享教师人数超限制"
	 */
	TEACHER_COUNT_EXCEED("200028E","共享教师人数超限制"),
	/**
	 * "610002E","该账号没有权限操作本场考试"
	 */
	PERMISSION_DENIED("610002E", "该账号没有权限操作本场考试"),
	/**
	 * "610003E","考试类型错误"
	 */
	EXAM_TYPE_ERROR("610003E", "考试类型错误"),
	/**
	 * "100010E","微信用户不存在"
	 */
	WECHAT_USER_NOT_FOUND("100010E", "微信用户不存在"),
	/**
	 * 更新记录为0
	 */
	UPDATE_RESULT_ZERO("600006E","更新记录为0"),
	/**
	 * 优惠券输入有误
	 */
	COUPON_INPUT_ERROR("900001E","优惠券输入有误"),
	/**
	 * 优惠码已失效
	 */
	COUPON_FAILURE("900002E","优惠码已失效"),
	/**
	 * 优惠码未到使用期
	 */
	COUPON_INVALID("900003E","该优惠码未到使用期,暂不能使用"),
	/**
	 * 超出最大使用数
	 */
	COUPON_USED_UP("900004E","该优惠码已发放完毕!"),
	/**
	 * 套餐类型不匹配
	 */
	COUPON_VERSION_MISMATCH("900005E","套餐类型不匹配"),
	/**
	 * 用户不匹配
	 */
	COUPON_USER_MISMATCH("900006E","该优惠码不适用当前用户"),
	/**
	 * 超过机构最大使用次数
	 */
	COUPON_BEYOND_PER_TIME("900007E","超过机构最大使用次数"),
	/**
	 * 超过机构最大使用次数
	 */
	COUPON_BEYOND_BUY_COUNT("900008E","仅支持购买x套"),
	/**
	 * "Z10003E","请求支付失败"
	 */
	ALIPAY_FAILURE("Z10003E","请求支付失败"),
	/**
	 * "Z10005E","订单号有重复"
	 */
	OUTTRADENO_DUPLICATE ("Z10005E","订单号有重复")
	
	;

	private String code;
	private String message;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	private ResultCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	private ResultCodeEnum() {
	}

}
