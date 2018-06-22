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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.base.BaseFragment;
import com.lease.R;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.util.MyApplication;
import com.util.Util;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

public class OrderFragment extends BaseFragment implements OnClickListener,
		IXListViewListener, OnItemClickListener {
	View view;
	Message msg;
	ImageView refresh;
	XListView listview;
	TextView titTxt;
	String[] idlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.note_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		init();
		creatMsg(0, 0);

		return view;
	}

	void init() {
		refresh = (ImageView) view.findViewById(R.id.back_btn);
		listview = (XListView) view.findViewById(R.id.note_lv);
		listview.setPullLoadEnable(true);
		titTxt = (TextView) view.findViewById(R.id.title_txt);
		refresh.setVisibility(View.VISIBLE);
		titTxt.setText("我的账单");
		refresh.setOnClickListener(this);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Util.showProssbar(this.getActivity());
			creatMsg(0, 100);
			break;

		default:
			break;
		}
	}

	List<Map<String, String>> getData() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		Object result = NetWork.NetResult("bill/getBillList/", null, null);
		if (result == null) {
			Util.showMsg(MyApplication.getAppContext(), "服务器或网络异常！");
		} else {
			JSONArray list = Util.json2ary(result.toString());
			idlist = new String[list.length()];
			if (list.length() != 0) {
				view.findViewById(R.id.note_hint).setVisibility(View.GONE);
				for (int i = 0; i < idlist.length; i++) {
					map = new HashMap<String, String>();
					try {
						JSONObject mark = (JSONObject) list.get(i);
						idlist[i] = (String) mark.getString("orderNo");
						map = new HashMap<String, String>();
						map.put(Util.MEAL_CONTENT[0],
								(String) mark.getString("totalUsedTime")
										+ " 小时");
						map.put(Util.MEAL_CONTENT[1],
								(String) mark.getString("currentTotalCost")
										+ " 元");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					data.add(map);
				}
			}
		}
		return data;
	}

	void RefreListview() {
		listview.setAdapter(new SimpleAdapter(this.getActivity(), getData(),
				R.layout.item_order_list, Util.MEAL_CONTENT, new int[] {
						R.id.item_order_time, R.id.item_order_cost }));
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
				RefreListview();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Util.id = idlist[position - 1];
		Jpage(new HistroyOrderFragment());
	}
}
