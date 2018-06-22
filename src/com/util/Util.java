package com.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dto.InOrderDto;
import com.dto.OrderDto;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.DivDialog;

public class Util {
	static long lastClickTime;
	public static OrderDto orderdto = new OrderDto();
	public static InOrderDto inorderdto = new InOrderDto();
	public static Bundle bundle = new Bundle();
	private static DivDialog prossbar;
	public static SlidingMenu slidingMenu;
	public static int slidingTag = 0;
	public static boolean mainTag = false;
	public static String[] params = new String[2];
	public static int chooseTag = 0;
	public static boolean tktag;
	public static List<String> userlist;
	public static String recharType = "";
	public static String recharCash = "";
	public static String uptype = "未上传";
	public static Map<String, Bitmap> btmap = new HashMap<String, Bitmap>();
	public static String id = "";
	public static String[] idlist;
	public static String idpro, idcity;
	public static String indexType = "分时租赁";
	public static Map<String, String> imgPath = new HashMap<String, String>();
	public static Map<Integer, List<String>> choosemap;
	public static String fragmentTag = "";
	public static String[] HOME_DEVICE = { "txt1", "txt2" };
	public static String[] NOTE_CONTENT = { "txt1" };
	public static String[] MEAL_CONTENT = { "txt1", "txt2", "txt3", "txt4" };
	public static String[] HISTORY_BILL = { "txt1", "txt2", "txt3", "txt4",
			"txt5" };
	public static String[] HOME_ITEM = { "txt1", "txt2", "txt3", "txt4",
			"txt5", "txt6", "txt7", "txt8", "tex9" };
	public static String[] mealname;
	public static String[] mealid;
	public static String meal_id = "";
	public static List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	
	public static String infoUrl = "";
	public static int helpType;		//0:newuser, 1:qa, 2:servicerule
	public static String curUserID = "";	//注册成功后,将用户ID返回到登录页面

	// public static HashMap<Integer, Boolean> isSelected = new HashMap<Integer,
	// Boolean>();
	// public static boolean IsMealPage = false;

	public static void showProssbar(Context context) {
		prossbar = new DivDialog(context);
		prossbar.show();
	}

	public static void closeProssbar() {
		if (prossbar != null)
			prossbar.dismiss();
	}

	public static void showMsg(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static boolean checklogin() {
		boolean b = false;
		if (userlist.size() == 0) {
			b = true;
			showMsg(MyApplication.getAppContext(), "您还未登录，请您登录！");
		}
		return b;
	}

	public static void hideKeyboard(View keyview, Context context) {
		if (keyview != null) {
			InputMethodManager inputmanger = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(keyview.getWindowToken(), 0);
		}
	}

	public static JSONArray json2ary(String jsonString) {
		JSONArray marks = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			marks = jsonObject.getJSONArray("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return marks;
	}

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 1000) {
			showMsg(MyApplication.getAppContext(), "操作过快，请您耐心等候！");
			return true;
		}
		lastClickTime = time;
		return false;
	}
	
	/** 
	 * 验证手机格式
	 * Author: SUNZHE
	 * Date: 2017-03-24 
	 */  
	public static boolean isMobileNo(String mobiles) {  
	    /* 
			    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
			    联通：130、131、132、152、155、156、185、186 
			    电信：133、153、180、189、（1349卫通） 
			    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
	    */  
	    String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) return false;  
	    else return mobiles.matches(telRegex);  
	} 
	
	/** 
	 * 验证邮箱ID格式
	 * Author: SUNZHE
	 * Date: 2017-03-24 
	 */ 
	public static boolean isEmail(String email) {
		//String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		String emailRegex = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";
	    if (TextUtils.isEmpty(email)) return false;  
	    else return email.matches(emailRegex); 
	}
	 
}