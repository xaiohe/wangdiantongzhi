package com.everflourish.act.app.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public abstract class AuthorService {
	private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);
	@Value("${wechat.appid}")
	private String appid;
	
	@Value("${wechat.secret}")
	private String secret;
	//执行授权处理
	public String getOppenId(HttpServletRequest req,String code){
		//会话中获取OPENID
		String oppenId= UserIdContext.getOppenId(req);
		if(oppenId == null){
			//获取OPENID
			oppenId = this.getOppenId(code);
			if(oppenId != null){
				UserIdContext.setOppenId(req, oppenId);
				return oppenId;
			}else{
				return null;
			}			
		}
		return oppenId; 
	}
	//获取OPENID
	public abstract String getOppenId(String code);

	protected String getAppid() {
		return appid;
	}

	protected String getSecret() {
		return secret;
	}
	
	
}
