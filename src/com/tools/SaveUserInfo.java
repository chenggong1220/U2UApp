package com.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.SharedPreferences;

import com.util.MyApplication;
import com.util.Util;

public class SaveUserInfo {
	static SharedPreferences preferences = MyApplication.getAppContext()
			.getSharedPreferences("lease",
					MyApplication.getAppContext().MODE_PRIVATE);
	static SharedPreferences.Editor sharedEdit = preferences.edit();

	public static List<String> relist() {
		List<String> list = new ArrayList<String>(preferences.getStringSet(
				"userinfo", new TreeSet<String>()));
		Collections.sort(list);
		return list;
	}

	public static void reuser() {
		Util.params[0] = preferences.getString("user", "");
		Util.params[1] = preferences.getString("pwd", "");
	}

	public static void clear() {
		Util.userlist.clear();
		sharedEdit.clear();
		sharedEdit.commit();
	}

	public static void putInfo(Set<String> dtoset) {
		sharedEdit.putStringSet("userinfo", dtoset);
		sharedEdit.commit();
	}

	public static void putUser(String user, String pwd) {
		Util.params[0] = user;
		Util.params[1] = pwd;
		sharedEdit.putString("user", user);
		sharedEdit.putString("pwd", pwd);
		sharedEdit.commit();
	}
	
	public static void setFirstOpenFlag(boolean firstOpen) {
		sharedEdit.putBoolean("firstOpen", firstOpen);
		sharedEdit.commit();
	}
	
	public static boolean isFirstOpen() {
		return preferences.getBoolean("firstOpen", true);
	}		
}
