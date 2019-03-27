package com.everflourish.act.app.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**  
 * @Title:  PhoneService.java   
 * @Package com.everflourish.act.app.service   
 * @Description:  短信业务层
 * @author: yangming
 * @date:   2019年3月14日 上午10:26:06   
 * @version V1.0  
 */
@Service
public class PhoneService {
	// 产品名称:云通信短信API产品,开发者无需替换
	private static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	private static final String domain = "dysmsapi.aliyuncs.com";
	private static final Logger logger = LoggerFactory.getLogger(PhoneService.class);
	@Value("${sms.appkey}")
	private String appkey;
	@Value("${sms.appSecret}")
	private String appSecret;
	/**
	 * 模板签名
	 */
	@Value("${sms.freeSignName}")
	private String freeSignName;
	/**
	 * 短信模板id
	 */
	@Value("${sms.templateCode}")
	private String templateCode;
	/**
	 * 有效的分钟
	 */
	@Value("${sms.minute}")
	private int minute;
	public void sendMessageByAliDayu(String phoneNo) throws ClientException {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appkey, appSecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phoneNo);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(freeSignName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		//request.setTemplateParam("{\"code\":\"" + code + "\"}");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("yourOutId");

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		sendSmsResponse.getMessage();
		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			logger.info("发送短信成功: phoneNo={} ", phoneNo);
		} else { // 发送短信失败
			String message = sendSmsResponse.getMessage();
			logger.error("发送短信失败: phone={},err={}", phoneNo, message);
		}
	}
	public static void main(String[] args) throws ClientException {
		PhoneService p = new PhoneService();
		p.sendMessageByAliDayu("17602068893");
	}
}
