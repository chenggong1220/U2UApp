package com.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.adapter.MyorderAdapter;
import com.base.BaseFragment;
import com.lease.R;
import com.main.MainActivity;
import com.net.NetWork;
import com.util.Util;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

public class MyOrderFragment extends BaseFragment implements OnClickListener,
		IXListViewListener {
	View view;
	Message msg;
	XListView listview;
	MainActivity activity;
	FragmentManager fm;
	FragmentTransaction ft;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.myorder_fragment, null);

		init();
		creatMsg(0, 0);
		return view;
	}

	void init() {
		activity = (MainActivity) getActivity();
		fm = activity.getSupportFragmentManager();
		ft = fm.beginTransaction();
		listview = (XListView) view.findViewById(R.id.myorder_lv);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
	}

	List<Map<String, String>> getData() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		Object result = NetWork.NetResult("order/getOrderList/", null, null);
		if (result == null) {
			Util.showMsg(this.getActivity(), "服务器或网络异常！");
		} else {
			JSONArray list = Util.json2ary(result.toString());
			Util.idlist = new String[list.length()];
			if (Util.idlist.length == 0) {
				map = new HashMap<String, String>();
				map.put(Util.HOME_ITEM[0], "");
				map.put(Util.HOME_ITEM[1], "");
				map.put(Util.HOME_ITEM[2], "0");
				map.put(Util.HOME_ITEM[3], "暂无订单");
				data.add(map);
			} else {
				for (int i = 0; i < Util.idlist.length; i++) {
					map = new HashMap<String, String>();
					try {
						JSONObject mark = (JSONObject) list.get(i);
						Util.idlist[i] = (String) mark.getString("orderId");
						map.put(Util.HOME_ITEM[0],
								(String) mark.getString("orderStatus"));
						map.put(Util.HOME_ITEM[1], "color");
						if (i == (Util.idlist.length - 1))
							map.put(Util.HOME_ITEM[2], "0");
						else
							map.put(Util.HOME_ITEM[2], "1");
						map.put(Util.HOME_ITEM[3],
								(String) mark.getString("orderNo"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					data.add(map);
				}
			}
		}
		return data;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new MineFragment());
			break;

		default:
			break;
		}
	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				listview.setAdapter(new MyorderAdapter(getData(), activity, ft));
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};

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
