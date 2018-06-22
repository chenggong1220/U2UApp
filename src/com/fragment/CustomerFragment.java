package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.base.BaseFragment;
import com.lease.R;
import com.slidingmenu.lib.SlidingMenu;
import com.util.Util;

public class CustomerFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.slidingTag = 1;
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.customer_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		init();
		msg = new Message();
		msg.what = 0;
		handler.sendMessageDelayed(msg, 0);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.customer_phone:
			// startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri
			// .parse("tel:" + "400-133-1567")));
			break;

		default:
			break;
		}
	}

	void init() {
		view.findViewById(R.id.customer_phone).setOnClickListener(this);
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
