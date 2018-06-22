package com.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.base.BaseFragment;
import com.dto.MealDto;
import com.lease.R;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.CreatView;
import com.util.MyApplication;
import com.util.Util;

public class MealFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;
	ViewPager viewPager;
	List<View> pageview = new ArrayList<View>();
	LayoutInflater inflater;
	int page = 0;
	MealDto dto = new MealDto();
	JSONArray list = new JSONArray();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.meal_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		this.inflater = inflater;

		msg = new Message();
		msg.what = 0;
		handler.sendMessageDelayed(msg, 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new DeviceChooseFragment());
			break;

		case R.id.meal_left_btn:
			if (page >= 0) {
				page--;
				viewPager.setCurrentItem(page);
			}
			break;

		case R.id.meal_right_btn:
			if (page < Util.mealname.length) {
				page++;
				viewPager.setCurrentItem(page);
			}
			break;

		case R.id.meal_sure:
			String name;
			if (page != 0) {
				name = Util.mealname[page - 1];
				Util.meal_id = Util.mealid[page - 1];
			} else {
				name = Util.mealname[0];
				Util.meal_id = Util.mealid[0];
			}
			Util.bundle.putString("meal", "meal");
			DeviceChooseFragment.makelist(Util.chooseTag, name);
			Jpage(new DeviceChooseFragment());
			break;
		default:
			break;
		}
	}

	void getData() {
		dto.assetTypeId = Util.id;
		dto.rentType = Util.indexType.contains("时") ? "0" : "1";
		Object result = NetWork.NetResult("meal/getMealInfoByAssetTypeId/",
				null, dto);
		if (result == null)
			Util.showMsg(MyApplication.getAppContext(), "服务器或网络异常！");
		else
			list = Util.json2ary(result.toString());
	}

	void init() {
		getData();
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		if (list.length() == 0) {
			view.findViewById(R.id.meal_sure).setVisibility(View.INVISIBLE);
		} else {
			Util.mealname = new String[list.length()];
			Util.mealid = new String[list.length()];
			view.findViewById(R.id.meal_hint).setVisibility(View.GONE);
			for (int i = 0; i < list.length(); i++) {
				try {
					pageview.add(CreatView.creat(inflater,
							(JSONObject) list.get(i), i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		// 绑定适配器
		viewPager.setAdapter(mPagerAdapter);
		// 设置viewPager的初始界面为第一个界面
		viewPager.setCurrentItem(0);
		// 添加切换界面的监听器
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		view.findViewById(R.id.back_btn).setOnClickListener(this);
		view.findViewById(R.id.meal_left_btn).setOnClickListener(this);
		view.findViewById(R.id.meal_right_btn).setOnClickListener(this);
		view.findViewById(R.id.meal_sure).setOnClickListener(this);
	}

	// 数据适配器
	PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		// 获取当前窗体界面数
		public int getCount() {
			return pageview.size();
		}

		@Override
		// 判断是否由对象生成界面
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// 使从ViewGroup中移出当前View
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageview.get(arg1));
		}

		// 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageview.get(arg1));
			return pageview.get(arg1);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				init();
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};

	public class MyOnPageChangeListener implements
			ViewPager.OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				page = 1;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 1:
				page = 2;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 2:
				page = 3;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 3:
				page = 4;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 4:
				page = 5;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 5:
				page = 6;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 6:
				page = 7;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 7:
				page = 8;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 8:
				page = 9;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 9:
				page = 10;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			case 10:
				page = 11;
				animation = new TranslateAnimation(0, 0, 0, 0);
				break;
			}
			// 将此属性设置为true可以使得图片停在动画结束时的位置
			animation.setFillAfter(true);
			// 动画持续时间，单位为毫秒
			animation.setDuration(200);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}