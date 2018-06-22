package com.util;

import android.app.Application;
import android.content.Context;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application implements
		Thread.UncaughtExceptionHandler {
	private static Context context;

	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		MyApplication.context = getApplicationContext();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public static Context getAppContext() {
		return MyApplication.context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ExitApplication.getInstance().exit();
	}
}