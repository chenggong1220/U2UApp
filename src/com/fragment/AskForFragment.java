package com.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.adapter.AskForAdapter;
import com.base.BaseFragment;
import com.dto.OrderDeviceDto;
import com.lease.R;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.ChooseAreaView;
import com.tools.DivDialog;
import com.tools.ListViewHeightBasedOnChildren;
import com.util.MyApplication;
import com.util.Util;

public class AskForFragment extends BaseFragment implements OnClickListener {
	View view;
	static LinearLayout arealay, llrentduration;
	static Message msg;
	static ListView listview, bailist, insurancelist;
	static TextView title, startime, endtime;
	static TextView cashplusTxt;
	static LayoutInflater inflater;
	static EditText detailAdr;
	PopupWindow window;
	static String seldate;
	CalendarView calendarView;
	static SimpleDateFormat formatter;
	static float totalDeposit = 0;
	static float totalInsurance = 0;
	static float[] cashlist;
	static SimpleAdapter adapter;
	static Calendar cal = Calendar.getInstance();
	static Map<String, Integer> DevPlus = new HashMap<String, Integer>();
	static DivDialog dialog;
	OrderDeviceDto dto;
	static Context context;
	static String isup;
	static Spinner rentDuration;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		context = this.getActivity();
		dialog = new DivDialog(this.getActivity());
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.askfor_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		init();
		creatMsg(0, 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mind_return:
			Jpage(new MindFragment());
			break;

		case R.id.askfor_plus:
			// Util.IsMealPage = true;
			Util.choosemap.clear();
			Util.bundle.putString("meal", "meal");
			Jpage(new DeviceChooseFragment());
			break;

		case R.id.mind_sure:
			String detail = detailAdr.getText().toString();
			String startTi = startime.getText().toString();
			String endTi = endtime.getText().toString();
			if (detail.equals("")) {
				Util.showMsg(this.getActivity(), "请填写详细存放地！");
				return;
			}
			try {
				if (formatter.parse(startTi).after(formatter.parse(endTi))) {
					Util.showMsg(this.getActivity(), "开始时间不能大于结束时间！");
					return;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			setDto();
			Util.orderdto.detailLocation = detail;
			Util.orderdto.province = Util.idpro;
			Util.orderdto.city = Util.idcity;
			Util.orderdto.startTime = startTi;
			Util.orderdto.endTime = endTi;
			Util.orderdto.allMoney = cashplusTxt.getText().toString()
					.split("元")[0];
			Util.orderdto.rentType = Util.indexType.contains("分时") ? "0" : "1";
			isup = null;
			//Start: SUNZHE, 2017-03-26
			Util.orderdto.rentDuraion = rentDuration.getSelectedItem().toString();
			Util.orderdto.totalDeposit = totalDeposit + "";
			Util.orderdto.totalInsurance = totalInsurance + "";
			//End: SUNZHE, 2017-03-26
			Jpage(new AskFor2Fragment());
			break;

		case R.id.askfor_caler:
			isup = "up";
			//PopuShow();	
			rentDuration.performClick();
			break;
		case R.id.llrentduration:
			isup = "up";
			//PopuShow();	
			rentDuration.performClick();
			break;
		case R.id.askfor_add:
			addTime(seldate);
			break;

		case R.id.askfor_sub:
			isup = "down";
			PopuShow();
			// subTime(seldate);
			break;

		case R.id.askfor_tk1:
			Util.tktag = true;
			startActivity(new Intent(this.getActivity(), AskForTK.class));
			break;

		case R.id.askfor_tk2:
			Util.tktag = false;
			startActivity(new Intent(this.getActivity(), AskForTK.class));
			break;

		default:
			break;
		}
	}

	static void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	void init() {
		title = (TextView) view.findViewById(R.id.leasing_type);
		startime = (TextView) view.findViewById(R.id.askfor_startime);
		endtime = (TextView) view.findViewById(R.id.askfor_endtime);
		cashplusTxt = (TextView) view.findViewById(R.id.askfor_cash_plus);
		listview = (ListView) view.findViewById(R.id.ask_lv);
		bailist = (ListView) view.findViewById(R.id.askfor_bail_lv);
		insurancelist = (ListView) view.findViewById(R.id.askfor_insurance_lv);
		arealay = (LinearLayout) view.findViewById(R.id.askfor_area);
		detailAdr = (EditText) view.findViewById(R.id.askfor_detail_address);
		rentDuration = (Spinner) view.findViewById(R.id.askfor_rentduration);
		view.findViewById(R.id.mind_sure).setOnClickListener(this);
		view.findViewById(R.id.askfor_sub).setOnClickListener(this);
		// view.findViewById(R.id.askfor_add).setOnClickListener(this);
		view.findViewById(R.id.askfor_plus).setOnClickListener(this);
		view.findViewById(R.id.mind_return).setOnClickListener(this);
		view.findViewById(R.id.askfor_caler).setOnClickListener(this);
		view.findViewById(R.id.askfor_tk1).setOnClickListener(this);
		view.findViewById(R.id.askfor_tk2).setOnClickListener(this);
		view.findViewById(R.id.llrentduration).setOnClickListener(this);
		
		
		List<String> list = new ArrayList<String>();
		list.add("6"); 
		list.add("12"); 
		list.add("18"); 
		list.add("24");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		rentDuration.setAdapter(adapter);
		rentDuration.setSelection(0);
	}

	static void setinfo() {
		listview.setAdapter(new AskForAdapter(getData()));
		setCashList();
		ListViewHeightBasedOnChildren.init(listview);
		ListViewHeightBasedOnChildren.init(bailist);
		ListViewHeightBasedOnChildren.init(insurancelist);
		title.setText(Util.indexType);
		cashplusTxt.setText((totalDeposit + totalInsurance) + "元");
		getDateString();
		addTime(seldate);
	}

	static List<Map<String, String>> getDail() {
		int deviceNum;
		int i = -1;
		List<String> list;
		cashlist = new float[Util.choosemap.size()];
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		for (int key : Util.choosemap.keySet()) {
			i++;
			map = new HashMap<String, String>();
			list = Util.choosemap.get(key);
			map.put(Util.HOME_DEVICE[0], list.get(0));
			String cash = list.get(2);
			float _cash = (Float.valueOf(cash))
					* (deviceNum = DevPlus.containsKey(list.get(0)) ? DevPlus
							.get(list.get(0)) : 1);
			map.put(Util.HOME_DEVICE[1], _cash + "元");
			cashlist[i] = _cash;
			data.add(map);
		}
		calcTotalDeposit(cashlist);
		return data;
	}
	
	
	static List<Map<String, String>> getInsurance() {
		int deviceNum;
		int i = -1;
		List<String> list;
		cashlist = new float[Util.choosemap.size()];
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		for (int key : Util.choosemap.keySet()) {
			i++;
			map = new HashMap<String, String>();
			list = Util.choosemap.get(key);
			map.put(Util.HOME_DEVICE[0], list.get(0));
			String cash = list.get(5);
			float _cash = (Float.valueOf(cash))
					* (deviceNum = DevPlus.containsKey(list.get(0)) ? DevPlus
							.get(list.get(0)) : 1);
			map.put(Util.HOME_DEVICE[1], _cash + "元");
			cashlist[i] = _cash;
			data.add(map);
		}
		calcTotalInsurance(cashlist);
		return data;
	}	
	
	void setDto() {
		List<String> list;
		Util.orderdto.subOrders.clear();
		for (int key : Util.choosemap.keySet()) {
			list = Util.choosemap.get(key);
			dto = new OrderDeviceDto();
			dto.assetTypeId = list.get(3);
			dto.comboId = list.get(4);
			dto.count = DevPlus.size() == 0 ? "1" : String.valueOf(DevPlus
					.get(list.get(0)));
			Util.orderdto.subOrders.add(dto);
		}
	}

	static void calcTotalDeposit(float[] flst) {
		totalDeposit = 0;
		for (float f : flst) {
			totalDeposit = totalDeposit + f;
		}
	}
	
	static void calcTotalInsurance(float[] flst) {
		totalInsurance = 0;
		for (float f : flst) {
			totalInsurance = totalInsurance + f;
		}
	}	

	public static void hasDevNum(String name, int num) {
		dialog.show();
		DevPlus.put(name, num);
		creatMsg(1, 0);
	}

	static List<Map<String, String>> getData() {
		List<String> list;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		for (int key : Util.choosemap.keySet()) {
			map = new HashMap<String, String>();
			list = Util.choosemap.get(key);
			map.put(Util.HOME_DEVICE[0], list.get(0));
			map.put(Util.HOME_DEVICE[1], list.get(1));
			data.add(map);
		}
		return data;
	}

	static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				setinfo();
//				View areaView = ChooseAreaView.oncreat(inflater, context);
//				areaView.setVisibility(View.INVISIBLE);
				arealay.addView(ChooseAreaView.oncreat(inflater, context));
				Util.closeProssbar();
				break;

			case 1:
				setCashList();
				cashplusTxt.setText((totalDeposit + totalInsurance) + "元");
				dialog.dismiss();
				break;
			case 2:
				//加载省市信息
				break;				

			default:
				break;
			}
		};
	};

	void PopuShow() {
		View view1 = inflater.inflate(R.layout.calendarview, null);
		window = new PopupWindow(view1,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, true);
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		calendarView = (CalendarView) view1.findViewById(R.id.calendarView);
		window.showAtLocation(view.findViewById(R.id.askfor_popu),
				Gravity.CENTER, 0, 0);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				getDateString(calendarView.getDate());
				// addTime(seldate);
			}
		});
		view1.findViewById(R.id.cale_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
	}

	static void addTime(String sel) {
		if (sel != null) {
			String[] dats = sel.split("-");
			cal.set(Integer.valueOf(dats[0])+1, Integer.valueOf(dats[1])-1,
					Integer.valueOf(dats[2]));
//			if (Util.indexType.contains("时")) {
//				cal.add(Calendar.MONTH, +0);
//			} else
//				cal.add(Calendar.MONTH, +5);
			seldate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			endtime.setText(seldate);
		}
	}

	void subTime(String sel) {
		int cha = Util.indexType.contains("时") ? 2 : 7;
		String currt = startime.getText().toString();
		if (submonth(sel, currt) < cha)
			Util.showMsg(this.getActivity(), "租赁时间不能小于" + (cha - 1) + "个月");
		else {
			String[] dats = sel.split("-");
			cal.set(Integer.valueOf(dats[0]), Integer.valueOf(dats[1]),
					Integer.valueOf(dats[2]));
			if (Util.indexType.contains("时")) {
				cal.add(Calendar.MONTH, -2);
			} else
				cal.add(Calendar.MONTH, -7);
			seldate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			endtime.setText(seldate);
		}
	}

	int str2int(String str) {
		String[] smart = str.split("-");
		return Integer.valueOf(smart[0] + smart[1] + smart[2]);
	}

	static void getDateString(long... date) {
		Date sell;
		Date curr = new Date();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		seldate = formatter.format(curr);
		if (date.length != 0) {
			sell = new Date(date[0]);
			if (sell.after(curr) || sell.equals(curr)) {
				seldate = formatter.format(sell);
			} else
				Util.showMsg(MyApplication.getAppContext(), "不能选择今天以前日期！");
		}
		if ("up".equals(isup) || null == isup)
			startime.setText(seldate);
		else
			endtime.setText(seldate);
	}

	static void setCashList() {
		adapter = new SimpleAdapter(MyApplication.getAppContext(), getDail(),
				R.layout.item_askfor_dail_list, Util.HOME_DEVICE, new int[] {
						R.id.dail_txt, R.id.dail_val });
		bailist.setAdapter(adapter);
		

		adapter = new SimpleAdapter(MyApplication.getAppContext(), getInsurance(),
				R.layout.item_askfor_insurance_list, Util.HOME_DEVICE, new int[] {
						R.id.insurance_txt, R.id.insurance_val });
		insurancelist.setAdapter(adapter);		
	}

	int submonth(String old, String now) {
		int result = 0;
		int month = 0;
		try {
			formatter = new SimpleDateFormat("yyyy-MM");
			Calendar bef = Calendar.getInstance();
			Calendar aft = Calendar.getInstance();
			bef.setTime(formatter.parse(old));
			aft.setTime(formatter.parse(now));
			result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
			month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Math.abs(month + result);
	}
}