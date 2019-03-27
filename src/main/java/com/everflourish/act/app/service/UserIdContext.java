package com.everflourish.act.app.service;

import javax.servlet.http.HttpServletRequest;

public class UserIdContext {
	private static final String USER_ID_KEY = "__SESSION__USER_ID__O__KEY__";
	
	public static void setOppenId(HttpServletRequest req,String oppenId){
		req.getSession().setAttribute(USER_ID_KEY,oppenId);
	}
	public static String getOppenId(HttpServletRequest req){
		String oppenId = (String) req.getSession().getAttribute(USER_ID_KEY);
		//String openid = TestOpenid.getRndOpenid();
		System.out.println("sessionId:----"+req.getSession().getId());
		return oppenId;
	}
}
