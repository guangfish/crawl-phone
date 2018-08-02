package com.adtime.crawl.phone.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class DataUpload {

	public static JSONObject loadData() throws IOException, JSONException {
		BufferedReader reader = null;
		OutputStreamWriter out = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(GlobalVar.DATA_SERVICE_LOADDATA).openConnection();
			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.setRequestMethod("POST"); // 设置请求方式
			conn.connect();
			// 读取响应
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			return new JSONObject(reader.readLine());
		} finally {
			if (out != null) {
				out.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public static void uploadData(JSONObject data) throws IOException {
		OutputStreamWriter out = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(GlobalVar.DATA_SERVICE_UPLOADDATA).openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST"); // 设置请求方式
			conn.connect();
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.append("data="+ URLEncoder.encode(data.toString(), "utf-8"));
			out.flush();
			out.close();
			conn.getResponseCode();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
