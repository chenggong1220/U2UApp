package com.fragment;

import com.base.BaseFragment;
import com.lease.R;
import com.slidingmenu.lib.SlidingMenu;
import com.util.Util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HelpFragment extends BaseFragment implements OnClickListener {
		View view;
		TextView contentTxt;
		Message msg;
		LayoutInflater curInflater;
		

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Util.slidingTag = 3;
			Util.fragmentTag = this.toString().split("[{]")[0];
			view = inflater.inflate(R.layout.help_fragment, null);
			Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

			init();
			msg = new Message();
			msg.what = 0;
			handler.sendMessageDelayed(msg, 0);

			curInflater = inflater;
			return view;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_help_newuser:		
				Jpage(new HelpNewUserFragment());
				break;
			case R.id.ll_help_qa:
				Jpage(new HelpQAFragment());
				break;
			case R.id.ll_help_servicerule:
				Jpage(new HelpServiceRuleFragment());
				break;				
			default:
				break;
			}			
		}

		void init() {
			view.findViewById(R.id.ll_help_newuser).setOnClickListener(this);
			view.findViewById(R.id.ll_help_qa).setOnClickListener(this);
			view.findViewById(R.id.ll_help_servicerule).setOnClickListener(this);
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
