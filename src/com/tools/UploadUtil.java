package com.tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.dto.ReImageDto;
import com.fragment.AskFor2Fragment;
import com.fragment.UpLoadFragment;
import com.google.gson.Gson;
import com.util.MyApplication;
import com.util.Util;

public class UploadUtil {
	static InputStream is;
	static DataOutputStream dos;
	static HttpURLConnection conn;
	static FileInputStream fis;
	static DivDialog dialog;
	static Gson gson = new Gson();
	static ReImageDto redto;
	static String type;

	public static void uploadFile(String filePath, String fileKey,
			String RequestURL, Context context, String tag) {
		type = tag;
		if (filePath == null) {
			Util.showMsg(MyApplication.getAppContext(), "文件不存在");
			return;
		}
		try {
			File file = new File(filePath);
			uploadFile(file, fileKey, RequestURL, context);
		} catch (Exception e) {
			Util.showMsg(MyApplication.getAppContext(), "文件不存在");
			e.printStackTrace();
			return;
		}
	}

	public static void uploadFile(final File file, final String fileKey,
			final String RequestURL, Context context) {
		if (file == null || (!file.exists())) {
			Util.showMsg(MyApplication.getAppContext(), "文件不存在");
			return;
		}
		dialog = new DivDialog(context);
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				toUploadFile(file, RequestURL);
			}
		}).start();
	}

	static final Handler msgHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 0:
				if (redto != null && redto.errorCode.equals("0")) {
					Util.imgPath.put(type, redto.message);
					Util.showMsg(MyApplication.getAppContext(), "上传成功！");
					AskFor2Fragment.setUpTxt();
					UpLoadFragment.Okup();
				} else
					Util.showMsg(MyApplication.getAppContext(), "服务器或网络异常！");
				if (dialog != null)
					dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	static void toUploadFile(File xml, String url_conn) {
		try {
			String boundary = "Boundary-b1ed-4060-99b9-fca7ff59c113";
			String Enter = "\r\n";
			fis = new FileInputStream(xml);
			URL url = new URL(url_conn);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setReadTimeout(10 * 1000);
			conn.setConnectTimeout(10 * 1000);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.connect();
			dos = new DataOutputStream(conn.getOutputStream());
			String part1 = "--" + boundary + Enter
					+ "Content-Type: application/octet-stream" + Enter
					+ "Content-Disposition: form-data; filename=\""
					+ xml.getName() + "\"; name=\"file\"" + Enter + Enter;
			String part2 = Enter + "--" + boundary + Enter
					+ "Content-Type: text/plain" + Enter
					+ "Content-Disposition: form-data; name=\"dataFormat\""
					+ Enter + Enter + "hk" + Enter + "--" + boundary + "--";
			byte[] xmlBytes = new byte[fis.available()];
			fis.read(xmlBytes);
			dos.writeBytes(part1);
			dos.write(xmlBytes);
			dos.writeBytes(part2);
			dos.flush();
			Thread.sleep(100);
			is = conn.getInputStream();
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			if (conn.getResponseCode() == 200) {
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				redto = gson.fromJson(sb1.toString(), ReImageDto.class);
				Message msg = msgHandler.obtainMessage();
				msg.arg1 = 0;
				msgHandler.sendMessage(msg);
				return;
			} else {
				Util.showMsg(MyApplication.getAppContext(), "服务器或网络异常！");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dos != null) {
					dos.flush();
					dos.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {

			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
