package com.everflourish.act.app.wechat;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 
* @author dsp
* @Date 2017-11-16 上午10:40:01
* @Version 0.1.0
 */
public class HttpClientUtil {
	
	public static String get(String uri) throws IOException, URISyntaxException{
		HttpClient client = HttpClients.createDefault();
		String charset = "UTF-8"; 
		HttpGet get = new HttpGet(uri);
		HttpResponse response;
		String responseBody = null ;
		try {
			get.setURI( new java.net.URI(uri));  
			response = client.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if(status == 200){
				responseBody = EntityUtils.toString(response.getEntity(),charset);
			}else{
				throw new IOException("http request error,status="+status);
			}
		} catch (ClientProtocolException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return responseBody;
	}

}
