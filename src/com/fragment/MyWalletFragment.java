package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.dto.MyWalletDto;
import com.dto.RMyWalletDto;
import com.lease.R;
import com.net.NetWork;
import com.util.Util;

public class MyWalletFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;
	TextView deposit, balance, bond;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.mywallet_fragment, null);

		init();
		creatMsg(0, 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new MineFragment());
			break;

		case R.id.register_btn_lay:
			Jpage(new RechargeFragment());
			break;

		default:
			break;
		}
	}

	void init() {
		deposit = (TextView) view.findViewById(R.id.mywallet_deposit);
		balance = (TextView) view.findViewById(R.id.mywallet_balance);
		bond = (TextView) view.findViewById(R.id.mywallet_bond);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
		view.findViewById(R.id.register_btn_lay).setOnClickListener(this);
	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	void getData() {
		RMyWalletDto dto = (RMyWalletDto) NetWork.NetResult(
				"auth/getWalletInfo/" + Util.params[0] + Util.params[1],
				RMyWalletDto.class, null);
		if (dto != null) {
			if (dto.errorCode.equals("0")) {
				MyWalletDto info = dto.message;
				deposit.setText(info.deposit + "元");
				balance.setText(info.balance + "元");
				bond.setText(info.bond + "元");
			} else
				Util.showMsg(this.getActivity(), dto.errorMsg);
		} else
			Util.showMsg(this.getActivity(), "服务器或网络异常！");
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getData();
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};
}
