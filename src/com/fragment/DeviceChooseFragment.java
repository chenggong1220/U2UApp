package com.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adapter.LegendAdapter;
import com.base.BaseFragment;
import com.dto.AssetListDto;
import com.lease.R;
import com.main.MainActivity;
import com.net.Config;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.BitmapHelper;
import com.util.Util;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

public class DeviceChooseFragment extends BaseFragment implements
		IXListViewListener {
	View view;
	Message msg;
	XListView listview;
	MainActivity activity;
	FragmentTransaction ft;
	static TextView nextTxt;
	String key;
	AssetListDto dto = new AssetListDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.choosedevice_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		if (getArguments() != null && getArguments().containsKey("meal"))
			key = getArguments().get("meal").toString();

		init();
		msg = new Message();
		msg.what = 0;
		handler.sendMessageDelayed(msg, 0);
		return view;
	}

	// 临时数据
	void getData() {
		if (key == null || !key.equals("meal")) {
			Util.data.clear();
			Map<String, Object> map = new HashMap<String, Object>();
			dto.rentTypeId = Util.indexType.contains("分时") ? "0" : "1";
			dto.page = "1";
			dto.pageSize = "20";
			Object result = NetWork.NetResult(
					"device/getAssetTypeListByRenType/", null, dto);
			if (result == null) {
				nextTxt.setEnabled(false);
				Util.showMsg(this.getActivity(), "服务器或网络异常！");
			} else {
				JSONArray list = Util.json2ary(result.toString());
				Util.idlist = new String[list.length()];
				for (int i = 0; i < Util.idlist.length; i++) {
					map = new HashMap<String, Object>();
					try {
						JSONObject mark = (JSONObject) list.get(i);
						Util.idlist[i] = (String) mark.getString("assetTypeId");
						map.put(Util.HOME_ITEM[0],
								(String) mark.getString("model"));
						map.put(Util.HOME_ITEM[1], "可选套餐");
						map.put(Util.HOME_ITEM[2], "green");
						map.put(Util.HOME_ITEM[3],
								(String) mark.getString("deposit"));
						map.put(Util.HOME_ITEM[4],
								(String) mark.getString("controlSystem"));
						map.put(Util.HOME_ITEM[5],
								(String) mark.getString("mainShaftSpeed"));
						map.put(Util.HOME_ITEM[6],
								(String) mark.getString("machinePower"));
						if (Util.btmap.get(Util.idlist[i]) == null)
							map.put(Util.HOME_ITEM[7], loadImage((String) mark
									.getString("picture")));
						else
							map.put(Util.HOME_ITEM[7],
									Util.btmap.get(Util.idlist[i]));
						map.put(Util.HOME_ITEM[8],
								(String) mark.getString("insurance"));
						//Util.showMsg(this.getActivity(), (String) mark.getString("insurance"));
						Util.data.add(map);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void makelist(int index, String name) {
		if (Util.data.size() != 0) {
			Util.data.get(index).put(Util.HOME_ITEM[1], name);
			Util.data.get(index).put(Util.HOME_ITEM[2], "red");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Util.bundle.clear();
			Jpage(new IndexFragment());
			break;

		case R.id.choose_next:
			if (checkcommit()) {
				Jpage(new MindFragment());
			} else
				Util.showMsg(this.getActivity(), "请选择租赁物及套餐！");
			break;
		default:
			break;
		}
	}

	void init() {
		activity = (MainActivity) getActivity();
		FragmentManager fm = activity.getSupportFragmentManager();
		ft = fm.beginTransaction();
		listview = (XListView) view.findViewById(R.id.choose_lv);
		nextTxt = (TextView) view.findViewById(R.id.choose_next);
		listview.setPullLoadEnable(true);
		nextTxt.setOnClickListener(this);
		listview.setXListViewListener(this);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
	}

	public static void changeTxt(Map<Integer, List<String>> map) {
		Util.choosemap = map;
		if (map.size() != 0)
			nextTxt.setText("选择(" + map.size() + ")");
		else
			nextTxt.setText("选择");
	}

	boolean checkcommit() {
		boolean b = false;
		if (nextTxt.getText().equals("选择"))
			return false;
		if (Util.choosemap != null && Util.choosemap.size() != 0) {
			for (int key : Util.choosemap.keySet()) {
				List<String> list = Util.choosemap.get(key);
				String s = list.toString();
				b = s.contains("可选套餐") ? false : true;
				if (b == false)
					break;
			}
		}
		return b;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getData();
				listview.setAdapter(new LegendAdapter(Util.data, activity, ft));
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};

	@SuppressWarnings("unchecked")
	Bitmap loadImage(String imgUrl) {
		Object obj = null;
		try {
			obj = new BitmapHelper(Config.ImgUrl + imgUrl).execute().get();
		} catch (Exception e) {
		}
		return (Bitmap) obj;
	}

	// 上一页
	@Override
	public void onRefresh() {
		Util.showMsg(this.getActivity(), "已经是第一页！");
		listview.stopRefresh();
	}

	// 下一页
	@Override
	public void onLoadMore() {
		Util.showMsg(this.getActivity(), "已经没有更多！");
		listview.stopLoadMore();
	}
}
