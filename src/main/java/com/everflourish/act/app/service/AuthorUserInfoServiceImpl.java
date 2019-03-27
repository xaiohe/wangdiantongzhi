package com.everflourish.act.app.service;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.everflourish.act.app.wechat.WechatIntfUtil;

/**
 * 普通的授权
 * @author Administrator
 *
 */
@Service
public class AuthorUserInfoServiceImpl extends AuthorService{

	@Override
	public String getOppenId(String code) {
		if(code != null) {
			String uri = "https://api.weixin.qq.com/sns/jscode2session?appid="
					+super.getAppid()
					+"&secret="+super.getSecret()
					+"&js_code="+code+"&grant_type=authorization_code";
			JSONObject json;
			try {
				String responseBody = WechatIntfUtil.httpRequest(uri, WechatIntfUtil.GET, null);//com.everflourish.act.util.HttpClientUtil.get(uri);
				json = JSONObject.parseObject(responseBody);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
				
			//网页授权错误
			if(json.containsKey("errcode")){
				throw new RuntimeException(json.toString());
			}
			String openId = json.getString("openid");
			return openId;
		}else {
			return null;
		}
	}

}
