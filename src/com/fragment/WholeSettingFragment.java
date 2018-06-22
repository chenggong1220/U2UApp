package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.BaseFragment;
import com.dto.WholeSettingDto;
import com.lease.R;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.util.Util;

public class WholeSettingFragment extends BaseFragment implements
		OnClickListener {
	View view;
	Message msg;
	ImageView imagview;
	boolean on2off;
	WholeSettingDto dto = new WholeSettingDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.slidingTag = 4;
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.whole_setting, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		init();
		creatMsg(0, 0);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.whole_switch:
			dto.username = Util.params[0];
			Util.showProssbar(this.getActivity());
			if (on2off == false) {
				dto.pushMessage = "1";
				Util.userlist.set(5, "61");
				on2off = true;
				creatMsg(1, 0);
			} else {
				dto.pushMessage = "0";
				Util.userlist.set(5, "60");
				on2off = false;
				creatMsg(2, 0);
			}
			break;

		case R.id.whole_changpwd_lay:
			Jpage(new ChangePwdFragment());
			break;

		default:
			break;
		}
	}

	void setNet(String str) {
		WholeSettingDto repho = (WholeSettingDto) NetWork.NetResult(
				"auth/changePushMessage/" + Util.params[0] + Util.params[1],
				WholeSettingDto.class, dto);
		if (repho != null) {
			if (repho.errorCode.equals("0"))
				Util.showMsg(this.getActivity(), "消息推送 : " + str);
			else
				Util.showMsg(this.getActivity(), repho.errorMsg);
		} else
			Util.showMsg(this.getActivity(), "服务器或网络异常！");
	}

	void init() {
		imagview = (ImageView) view.findViewById(R.id.whole_switch);
		on2off = Util.userlist.get(5).equals("60") ? false : true;
		if (on2off == false)
			imagview.setBackgroundResource(R.drawable.switch_off);
		else
			imagview.setBackgroundResource(R.drawable.switch_on);
		imagview.setOnClickListener(this);
		view.findViewById(R.id.whole_changpwd_lay).setOnClickListener(this);
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
				Util.closeProssbar();
				break;

			case 1:
				imagview.setBackgroundResource(R.drawable.switch_on);
				setNet("开");
				Util.closeProssbar();
				break;

			case 2:
				imagview.setBackgroundResource(R.drawable.switch_off);
				setNet("关");
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};
}
