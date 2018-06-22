package com.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.OrderInfoAdapter;
import com.base.BaseFragment;
import com.dto.OrderInfoDto;
import com.dto.ROrderInfoDto;
import com.lease.R;
import com.net.NetWork;
import com.util.Util;

public class OrderInfoFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;
	ListView listview;
	TextView assetInfo, orderNo, leaseType, bond;
	String[] namelist = { "租赁申请", "订单处理", "项目处理", "项目复核", "信审", "信审复核",
			"合同到司确认", "合同签约", "保证金核销", "发货", "到货确认" };
	OrderInfoDto dto = new OrderInfoDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.order_info_fragment, null);

		init();
		creatMsg(0, 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new MyOrderFragment());
			break;

		default:
			break;
		}
	}

	List<Map<String, String>> getData() {
		String struts = "";
		dto.orderId = Util.id;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		ROrderInfoDto redto = (ROrderInfoDto) NetWork.NetResult(
				"order/getOrderById/" + Util.params[0] + Util.params[1],
				ROrderInfoDto.class, dto);
		if (redto != null) {
			if (redto.errorCode.equals("0")) {
				OrderInfoDto info = redto.message;
				struts = info.orderStatus;
				assetInfo.setText(info.assetInfo);
				orderNo.setText(info.orderNo);
				String type = (info.leaseType).equals("0") ? "分时租赁" : "分月租赁";
				leaseType.setText(type);
				bond.setText(info.bond + "元");
			} else
				Util.showMsg(this.getActivity(), redto.errorMsg);
		} else
			Util.showMsg(this.getActivity(), "服务器或网络异常！");

		for (int i = 0; i < namelist.length; i++) {
			if (i % 2 == 0) {
				map = new HashMap<String, String>();
				map.put(Util.MEAL_CONTENT[0], namelist[i]);
				if (struts.equals(namelist[i]))
					map.put(Util.MEAL_CONTENT[1], "red");
				else
					map.put(Util.MEAL_CONTENT[1], "green");
				map.put(Util.MEAL_CONTENT[2], "right");
			} else {
				map = new HashMap<String, String>();
				map.put(Util.MEAL_CONTENT[0], namelist[i]);
				if (struts.equals(namelist[i]))
					map.put(Util.MEAL_CONTENT[1], "red");
				else
					map.put(Util.MEAL_CONTENT[1], "green");

				map.put(Util.MEAL_CONTENT[2], "left");
			}
			data.add(map);
		}
		return data;
	}

	void init() {
		assetInfo = (TextView) view.findViewById(R.id.orderinfo_assetInfo);
		orderNo = (TextView) view.findViewById(R.id.orderinfo_orderNo);
		leaseType = (TextView) view.findViewById(R.id.orderinfo_leaseType);
		bond = (TextView) view.findViewById(R.id.orderinfo_bond);
		listview = (ListView) view.findViewById(R.id.order_info_lv);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
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
				listview.setAdapter(new OrderInfoAdapter(getData()));
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};
}
