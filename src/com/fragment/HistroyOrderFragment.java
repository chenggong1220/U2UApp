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
import android.widget.SimpleAdapter;

import com.base.BaseFragment;
import com.dto.AlipayInDto;
import com.dto.AplayDto;
import com.dto.HistoryBillDto;
import com.dto.ReAplayDto;
import com.lease.R;
import com.net.NetWork;
import com.pay.PayUtil;
import com.util.MyApplication;
import com.util.Util;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

public class HistroyOrderFragment extends BaseFragment implements
		OnClickListener, IXListViewListener, OnItemClickListener {
	View view;
	static View backbtn;
	Message msg;
	HistoryBillDto dto = new HistoryBillDto();
	XListView listview;
	String[] idlist, amountlist, stuslist;
	AplayDto aplaydto = new AplayDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.histroy_order, null);

		init();
		creatMsg(0, 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new OrderFragment());
			break;

		default:
			break;
		}
	}

	List<Map<String, String>> getData() {
		dto.page = "1";
		dto.pageSize = "20";
		dto.orderId = Util.id;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		Object result = NetWork.NetResult("bill/getHistoryBill/", null, dto);
		if (result == null) {
			Util.showMsg(MyApplication.getAppContext(), "服务器或网络异常！");
		} else {
			JSONArray list = Util.json2ary(result.toString());
			if (list.length() != 0) {
				view.findViewById(R.id.history_bill_hint).setVisibility(
						View.GONE);
				idlist = new String[list.length()];
				amountlist = new String[list.length()];
				stuslist = new String[list.length()];
				for (int i = 0; i < list.length(); i++) {
					map = new HashMap<String, String>();
					try {
						JSONObject mark = (JSONObject) list.get(i);
						map = new HashMap<String, String>();
						idlist[i] = (String) mark.getString("id");
						String amnt = (String) mark.getString("amount");
						map.put(Util.HISTORY_BILL[0], amnt + "元");
						amountlist[i] = amnt;
						String stus = mark.getString("payStatus").equals("0") ? "未支付"
								: "已支付";
						map.put(Util.HISTORY_BILL[1], stus);
						stuslist[i] = stus;
						map.put(Util.HISTORY_BILL[2],
								(String) mark.getString("accountPeriod"));
						map.put(Util.HISTORY_BILL[3],
								(String) mark.getString("accountPeriodStatus"));
						String payTime = mark.getString("payTime").split(" ")[0];
						map.put(Util.HISTORY_BILL[4], payTime);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					data.add(map);
				}
			}
		}
		return data;
	}

	void init() {
		backbtn = view.findViewById(R.id.back_btn);
		listview = (XListView) view.findViewById(R.id.history_bill_lv);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);
		backbtn.setOnClickListener(this);
	}

	public static void sucesse() {
		if (backbtn != null)
			backbtn.performClick();
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
				listview.setAdapter(new SimpleAdapter(
						HistroyOrderFragment.this.getActivity(),
						getData(),
						R.layout.item_history_bill_list,
						Util.HISTORY_BILL,
						new int[] { R.id.his_amount, R.id.his_payStatus,
								R.id.his_accountPeriod,
								R.id.his_accountPeriodStatus, R.id.his_payTime }));
				Util.closeProssbar();
				break;

			case 1:
				ReAplayDto redto = (ReAplayDto) NetWork.NetResult("pay/alipay/"
						+ Util.params[0] + Util.params[1], ReAplayDto.class,
						aplaydto);
				if (redto != null) {
					if (redto.errorCode.equals("0")) {
						AlipayInDto cashdto = redto.message;
						PayUtil.pay(HistroyOrderFragment.this.getActivity(),
								cashdto);
						Util.closeProssbar();
					} else {
						Util.showMsg(HistroyOrderFragment.this.getActivity(),
								redto.errorMsg);
						Util.closeProssbar();
					}
				} else {
					Util.showMsg(HistroyOrderFragment.this.getActivity(),
							"服务器或网络异常！");
					Util.closeProssbar();
				}
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
		if (stuslist[position - 1].contains("已支付"))
			return;
		Util.showProssbar(this.getActivity());
		aplaydto.type = "2";
		aplaydto.billCheckId = idlist[position - 1];
		aplaydto.amount = amountlist[position - 1];
		creatMsg(1, 0);
	}
}