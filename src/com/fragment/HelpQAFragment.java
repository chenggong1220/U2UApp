package com.fragment;

import com.base.BaseFragment;
import com.lease.R;
import com.slidingmenu.lib.SlidingMenu;
import com.util.Util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HelpQAFragment extends BaseFragment implements OnClickListener {
		View view;
		TextView contentTxt;
		Message msg;
		LayoutInflater curInflater;
		

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Util.slidingTag = 3;
			Util.fragmentTag = this.toString().split("[{]")[0];
			view = inflater.inflate(R.layout.help_qa, null);
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
			case R.id.back_btn:
				Jpage(new HelpFragment());			
				break;			
			default:
				break;
			}			
		}

		void init() {
			view.findViewById(R.id.back_btn).setOnClickListener(this);
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
