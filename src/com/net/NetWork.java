package com.net;

import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.dto.ResultDto;
import com.google.gson.Gson;
import com.util.Util;

public class NetWork {
	static String result = "";
	static String url;
	static Gson gson = new Gson();

	public static Object NetResult(String urlfix, Class m, Object c) {
		if (m != null)
			url = Config.Url + urlfix;
		else {
			if (c != null && c.equals("getStarAsset"))
				url = Config.Url + urlfix;
			else
				url = Config.Url + urlfix + Util.params[0] + Util.params[1];
		}
		try {
			result = new GeocodeingTask().execute(c).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (m != null)
			return keyResult1(result, m);
		else
			return keyResult2(result);
	}

	static <T> T keyResult1(String jsonString, T t) {
		if (jsonString.equals(""))
			return null;
		t = gson.fromJson(jsonString, (Class<T>) t);
		return t;
	}

	static String keyResult2(String jsonString) {
		if (jsonString.equals(""))
			return null;
		ResultDto dto = gson.fromJson(jsonString, ResultDto.class);
		if (dto.errorCode.equals("0")) {
			return jsonString;
		} else
			return null;
	}

	static class GeocodeingTask extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {
			String rejson = "";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				HttpResponse response;

				// 添加http头信息
				httppost.addHeader("Authorization", "your token");
				httppost.addHeader("Content-Type", "application/json");
				httppost.addHeader("User-Agent", "imgfornote");

				String obj = gson.toJson(params[0]);
				httppost.setEntity(new StringEntity(obj, "utf-8"));

				// 设置超时
				httpclient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);
				httpclient.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);

				response = httpclient.execute(httppost);
				// 检验状态码，如果成功接收数据
				if ((response.getStatusLine().getStatusCode()) == 200) {
					rejson = EntityUtils.toString(response.getEntity());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rejson;
		}
	}
}