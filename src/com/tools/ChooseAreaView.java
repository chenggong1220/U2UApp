package com.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.adapter.SpinnerAdapter;
import com.dto.CItem;
import com.lease.R;
import com.net.NetWork;
import com.util.MyApplication;
import com.util.Util;

public class ChooseAreaView {
	static DivDialog dialog;
	static Spinner spi_pro, spi_city;
	static SpinnerAdapter adapterEdu;
	static Message msg;
	static int cityposit = 0;
	static List<CItem> lstPro, lstCty;
	static CItem ct;

	public static View oncreat(LayoutInflater inflater, Context context,
			int... parm) {
		dialog = new DivDialog(context);
		View view = inflater.inflate(R.layout.view_area_choose, null);
		spi_pro = (Spinner) view.findViewById(R.id.view_area_pro);
		spi_city = (Spinner) view.findViewById(R.id.view_area_city);
		getData();
		creatAdpt(lstPro);
		spi_pro.setAdapter(adapterEdu);
//取消自动加载市信息，SUNZHE， 2017-03-26		
//		if (parm.length != 0) {
//			spi_pro.setSelection(findPost(parm[0], lstPro));
//			cityposit = parm[1];
//		}
		spi_pro.setOnItemSelectedListener(onitemselected);
		spi_city.setOnItemSelectedListener(onitemselected);
		return view;
	}

	static void getData() {
		Object result = NetWork.NetResult("location/getProvices", null,
				"getStarAsset");
		if (result == null) {
			Util.showMsg(MyApplication.getAppContext(), "服务器或网络异常！");
		} else {
			lstPro = new ArrayList<CItem>();
			JSONArray list = Util.json2ary(result.toString());
			if (list.length() != 0) {
				for (int i = 0; i < list.length(); i++) {
					ct = new CItem();
					try {
						JSONObject mark = (JSONObject) list.get(i);
						ct.ID = (String) mark.getString("id");
						ct.Value = (String) mark.getString("name");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					lstPro.add(ct);
				}
			}
		}
	}

	static void creatAdpt(List<CItem> datalist) {
		if (datalist != null) {
			adapterEdu = new SpinnerAdapter(MyApplication.getAppContext(),
					android.R.layout.simple_spinner_item, datalist);
			adapterEdu
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		}
	}

	static void getData2(String id) {
		Object result = NetWork.NetResult("location/getCities/" + id, null,
				"getStarAsset");
		if (result == null) {
			Util.showMsg(MyApplication.getAppContext(), "服务器或网络异常！");
		} else {
			lstCty = new ArrayList<CItem>();
			JSONArray list = Util.json2ary(result.toString());
			if (list.length() != 0) {
				for (int i = 0; i < list.length(); i++) {
					ct = new CItem();
					try {
						JSONObject mark = (JSONObject) list.get(i);
						ct.ID = (String) mark.getString("id");
						ct.Value = (String) mark.getString("name");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					lstCty.add(ct);
				}
			}
		}
	}

	static OnItemSelectedListener onitemselected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			switch (parent.getId()) {
			case R.id.view_area_pro:
				dialog.show();
				Util.idpro = ((CItem) spi_pro.getSelectedItem()).ID;
				creatMsg(0);
				break;

			case R.id.view_area_city:
				Util.idcity = ((CItem) spi_city.getSelectedItem()).ID;
				break;

			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	static void creatMsg(int what) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, 100);
	}

	static Integer findPost(int id, List<CItem> list) {
		if (id == 0)
			return 0;
		else {
			int post = -1;
			for (CItem ct : list) {
				post++;
				if (ct.ID.equals(String.valueOf(id)))
					break;
			}
			return post > -1 ? post : 0;
		}
	}

	static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getData2(Util.idpro);
				creatAdpt(lstCty);
				spi_city.setAdapter(adapterEdu);
				spi_city.setSelection(findPost(cityposit, lstCty));
				if (dialog != null)
					dialog.dismiss();
				break;

			default:
				break;
			}
		};
	};
}