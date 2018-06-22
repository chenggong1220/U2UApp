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
import com.lease.R;
import com.slidingmenu.lib.SlidingMenu;
import com.util.Util;

public class AboutFragment extends BaseFragment implements OnClickListener {
	View view;
	TextView contentTxt;
	Message msg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.slidingTag = 3;
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.about_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		init();
		msg = new Message();
		msg.what = 0;
		handler.sendMessageDelayed(msg, 0);

		return view;
	}

	@Override
	public void onClick(View v) {
	}

	void init() {
		contentTxt = (TextView) view.findViewById(R.id.about_content);
		contentTxt.setText("\u3000\u3000" + content());
		
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

	String content() {
		return "优尼斯融资租赁（上海）有限公司（简称“公司”）是由沈机集团（香港）有限公司和沈阳机床股份有限公司在上海自贸区投资设立的合资公司，"
				+ "是沈机集团战略转型升级的重要支点；公司总部设立在上海长风金融港，业务遍及全国各地。"
				+ "\n\u3000\u3000"
				+ "公司秉承\"坚持、容合、创新\"的企业文化，结合实时的市场情况专项推出B2B、O2O、U2U服务模式，简化流程、提高效率，给用户最佳的融资体验。"
				+ "公司融合沈阳机床集团先进的工业技术、卓越的品牌影响力和深厚的市场资源等优势，"
				+ "为装备制造业提供灵活多样的资金产品和增值服务，矢志成为融资租赁行业塑造\"智慧租赁、绿色资产\"的先行者和领跑者！";
	}

}
