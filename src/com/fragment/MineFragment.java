package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.lease.R;
import com.slidingmenu.lib.SlidingMenu;
import com.util.Util;

public class MineFragment extends BaseFragment {
	View view;
	Message msg;
	TextView sysUser, sysInfo, minetype;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.mine_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		sysUser = (TextView) view.findViewById(R.id.sys_user);
		sysInfo = (TextView) view.findViewById(R.id.sys_info);

		init();
		msg = new Message();
		msg.what = 0;
		handler.sendMessageDelayed(msg, 0);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mine_setting:
			Jpage(new SettingFragment());
			break;

		case R.id.mine_wallet:
			Jpage(new MyWalletFragment());
			break;

		case R.id.mine_order:
			Jpage(new MyOrderFragment());
			break;

		case R.id.mine_upload:
			Jpage(new UpLoadFragment());
			break;

		case R.id.mine_uptype:
			Jpage(new UpLoadFragment());
			break;

		default:
			break;
		}
	}

	void init() {
		if (Util.userlist.size() != 0) {
			sysInfo.setText("当前用户 : ");
			sysUser.setText(Util.userlist.get(0).substring(1));
			sysUser.setVisibility(View.VISIBLE);
		}
		minetype = (TextView) view.findViewById(R.id.mine_uptype);
		minetype.setText(Util.uptype);
		minetype.setOnClickListener(this);
		view.findViewById(R.id.mine_setting).setOnClickListener(this);
		view.findViewById(R.id.mine_wallet).setOnClickListener(this);
		view.findViewById(R.id.mine_upload).setOnClickListener(this);
		view.findViewById(R.id.mine_order).setOnClickListener(this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};
}
