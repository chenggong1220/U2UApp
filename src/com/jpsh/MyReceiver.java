package com.jpsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.main.MainActivity;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		String s1 = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		String s2 = bundle.getString(JPushInterface.EXTRA_ALERT);
		String title = s1 == null ? "" : s1;
		String content = s2 == null ? "" : s2;
		if (!title.equals("") || !content.equals(""))
			MainActivity.setRed();
	}
}
