package com.liyuncong.application.commmontools.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class NetUtil {
	private CloseableHttpClient httpClient;
	private HttpClientContext context;
	
	public NetUtil() {
		httpClient = HttpClients.createDefault();
		
		HttpContext httpContext = new BasicHttpContext();
		context = HttpClientContext.adapt(httpContext);
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public HttpClientContext getContext() {
		return context;
	}

	public void setContext(HttpClientContext context) {
		this.context = context;
	}
	
	public String sendRequest(HttpUriRequest httpUriRequest) {
		String content = "";
		// 执行请求用execute方法，content用来帮我们附带上额外信息
		try(CloseableHttpResponse response = httpClient.execute(httpUriRequest, context);) {
			// 得到相应实体、包括响应头以及相应内容
			HttpEntity entity = response.getEntity();
			// 得到response的内容
			content = EntityUtils.toString(entity);
			//　关闭输入流
			EntityUtils.consume(entity);
			return content;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return content;
	}
	
	public HttpPost getHttpPost(String url, Map<String, String> parameters) {
		if (url == null || url.length() == 0) {
			return null;
		}
		
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		if (parameters != null) {
			for(Entry<String, String> entry : parameters.entrySet()) {
				NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(),
						entry.getValue());
				nvps.add(nameValuePair);
			}
		}
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(5000).setConnectTimeout(5000)
				.setSocketTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return httpPost;
	}
	
	public HttpGet getHttpGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(5000).setConnectTimeout(5000)
				.setSocketTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		return httpGet;
	}
}
