package com.everflourish.act.app.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatGetReadListRequest;
import com.dingtalk.api.request.OapiChatGetRequest;
import com.dingtalk.api.request.OapiChatSendRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiChatGetReadListResponse;
import com.dingtalk.api.response.OapiChatGetResponse;
import com.dingtalk.api.response.OapiChatSendResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.taobao.api.ApiException;

/**  
 * @Title:  DingTalkUtil.java   
 * @Package com.everflourish.act.app.util   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: yangming
 * @date:   2019年3月14日 下午4:33:46   
 * @version V1.0  
 */
@Component
public class DingTalkUtil {
	@Value("${dd.chatId}")
	private static String ddChatId;
	@Value("${dd.appKey}")
	private static String ddAppKey;
	@Value("${dd.appSecret}")
	private static String ddAppSecret;
	private static String ACCESS_TOKEN = null;
	private static Long ACCESS_TOKEN_TIME = null;
	/**
	 * @Title: sendDingTalk   
	 * @Description: 发送群消息
	 * @param: @throws ApiException      
	 * @return: void      
	 * @throws
	 */
	public static String sendDingTalk(String content) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
		OapiChatSendRequest request = new OapiChatSendRequest();
		request.setChatid(ddChatId);

		OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
		msg.setMsgtype("text");
		OapiChatSendRequest.Text text = new OapiChatSendRequest.Text();
		text.setContent(content);
		msg.setText(text);

		request.setMsg(msg);
		OapiChatSendResponse response = client.execute(request, getAccessToken());
		JSONObject result = JSONObject.parseObject(response.getBody());
		return result.getString("messageId");
	}
	/**
	 * @Title: getAccessToken   
	 * @Description: 获取授权
	 * @param: @return
	 * @param: @throws ApiException      
	 * @return: String      
	 * @throws
	 */
	public static String getAccessToken() throws ApiException {
		// 为空或者超时重新获取，7200s超时
		if (ACCESS_TOKEN == null || ((System.currentTimeMillis() - ACCESS_TOKEN_TIME) / 1000) > 7000) {
			DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
			OapiGettokenRequest request = new OapiGettokenRequest();
			request.setAppkey(ddAppKey);
			request.setAppsecret(ddAppSecret);
			request.setHttpMethod("GET");
			OapiGettokenResponse response = client.execute(request);
			JSONObject result = JSONObject.parseObject(response.getBody());
			ACCESS_TOKEN = result.getString("access_token");
			ACCESS_TOKEN_TIME = System.currentTimeMillis();
		}
		return ACCESS_TOKEN;
	}
	/**
	 * @Title: getAlreadyRead   
	 * @Description: 查看消息已读用户
	 * @param: @param messId
	 * @param: @throws ApiException      
	 * @return: void      
	 * @throws
	 */
	public static JSONArray getAlreadyRead(String messId) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/getReadList");
		OapiChatGetReadListRequest request = new OapiChatGetReadListRequest();
		request.setHttpMethod("GET");
		request.setMessageId(messId);
		request.setCursor(0L);
		request.setSize(20L);
		OapiChatGetReadListResponse response = client.execute(request, getAccessToken());
		JSONObject result = JSONObject.parseObject(response.getBody());
		return result.getJSONArray("readUserIdList");
	}
	
	/**
	 * @Title: getGroupUser   
	 * @Description: 获取钉钉群用户
	 * @param: @return
	 * @param: @throws ApiException      
	 * @return: JSONObject      
	 * @throws
	 */
	public static Map<String,String> getGroupUser() throws ApiException {
		Map<String,String> maps = new HashMap<String,String>();
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/get");
		OapiChatGetRequest request = new OapiChatGetRequest();
		request.setHttpMethod("GET");
		request.setChatid(ddChatId);
		OapiChatGetResponse response = client.execute(request, getAccessToken());
		JSONArray result = JSONObject.parseObject(response.getBody()).getJSONObject("chat_info").getJSONArray("useridlist");
		for(Object str : result) {
			maps.put(str.toString(), getGroupUserInfo(str.toString()));
		}
		return maps;
	}
	
	/**
	 * @Title: getGroupUserInfo   
	 * @Description: 获取用户详细信息
	 * @param: @param userId
	 * @param: @return
	 * @param: @throws ApiException      
	 * @return: JSONObject      
	 * @throws
	 */
	public static String getGroupUserInfo(String userId) throws ApiException {
		// TODO Auto-generated method stub
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
		OapiUserGetRequest request = new OapiUserGetRequest();
		request.setUserid(userId);
		request.setHttpMethod("GET");
		OapiUserGetResponse response = client.execute(request, getAccessToken());
		JSONObject result = JSONObject.parseObject(response.getBody());
		return result.getString("name");
	}
	public static void main(String[] args) throws ApiException {
		System.out.println(getAccessToken());
	}
}
