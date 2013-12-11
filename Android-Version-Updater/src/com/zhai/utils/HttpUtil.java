package com.zhai.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public static String getResponse(String url)
			throws ClientProtocolException, IOException, HttpException {

		String strResult = "";
		HttpGet httpRequest = new HttpGet(url);

		httpRequest.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpRequest.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				3000);

		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		switch (httpResponse.getStatusLine().getStatusCode()) {
		case HttpStatus.SC_OK:// 返回Ok的数据
			strResult = EntityUtils.toString(httpResponse.getEntity());
			return strResult;
		case HttpStatus.SC_GATEWAY_TIMEOUT:
			throw new HttpException("网络超时");
		}
		return null;
	}

	public static InputStream getInputStreamFormUrl(String url)
			throws IOException {

		HttpGet httpRequest = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse httpResponse = httpClient.execute(httpRequest);

		switch (httpResponse.getStatusLine().getStatusCode()) {
		case HttpStatus.SC_OK:// 返回Ok的数据
			// strResult = EntityUtils.toString(httpResponse.getEntity());
			return httpResponse.getEntity().getContent();
			// break;
		default:
			throw new IOException("其他网络错误");
		}
	}

}
