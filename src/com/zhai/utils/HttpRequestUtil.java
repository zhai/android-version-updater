package com.zhai.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpRequestUtil {

	// 使用办法
	/*
	 * List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	 * nameValuePair.add(new BasicNameValuePair("isAndriod", "1"));
	 * nameValuePair.add(new BasicNameValuePair("username", mUsernameEdit
	 * .getText().toString())); nameValuePair.add(new
	 * BasicNameValuePair("password", mPasswordEdit .getText().toString()));
	 * nameValuePair.add(new BasicNameValuePair("scheme", RdpConfig.protocal));
	 * 
	 * String result = sendDataByPost(RdpConfig.protocal + "://" +
	 * mGatewayIP.getText().toString() + RdpConfig.loginPostUrl, nameValuePair);
	 */

	public static String sendDataByPost(String url, List<NameValuePair> datas) {
		// org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpClient client = createHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse resp = null;
		String result = "";
		// post data
		try {
			post.setEntity(new UrlEncodedFormEntity(datas, HTTP.UTF_8));
			resp = client.execute(post);
			result = EntityUtils.toString(resp.getEntity());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

}
