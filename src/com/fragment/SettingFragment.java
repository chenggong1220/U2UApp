package com.fragment;

import java.util.TreeSet;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.BaseFragment;
import com.dto.ReLoginDto;
import com.dto.SettingDto;
import com.lease.R;
import com.net.NetWork;
import com.tools.CheckList2;
import com.tools.ChooseAreaView;
import com.tools.SaveUserInfo;
import com.util.Util;

public class SettingFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;
	LinearLayout arealay;
	TextView title, commit;
	ImageView person, company;
	EditText name, nick, chepwd, email, phone;
	String isType, userType;
	LayoutInflater inflater;
	ReLoginDto dto = new ReLoginDto();
	SettingDto redto = new SettingDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.register_activity, null);

		init();
		creatMsg(0, 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_img:
			isType = "0";
			person.setBackgroundResource(R.drawable.register_check);
			company.setBackgroundResource(R.drawable.register_uncheck);
			break;

		case R.id.company_img:
			isType = "1";
			person.setBackgroundResource(R.drawable.register_uncheck);
			company.setBackgroundResource(R.drawable.register_check);
			break;

		case R.id.register_btn_lay:
			Util.showProssbar(this.getActivity());
			creatMsg(1, 0);
			break;

		case R.id.back_btn:
			Jpage(new MineFragment());
			break;

		default:
			break;
		}
	}

	void init() {
		name = (EditText) view.findViewById(R.id.register_name);
		nick = (EditText) view.findViewById(R.id.register_nick);
		chepwd = (EditText) view.findViewById(R.id.register_chepwd);
		email = (EditText) view.findViewById(R.id.register_email);
		phone = (EditText) view.findViewById(R.id.register_phone);
		arealay = (LinearLayout) view.findViewById(R.id.register_area_lay);
		
		view.findViewById(R.id.label_regsms).setVisibility(View.GONE);
		view.findViewById(R.id.register_sms).setVisibility(View.GONE);
		view.findViewById(R.id.label_regpwd).setVisibility(View.GONE);
		view.findViewById(R.id.register_pwd).setVisibility(View.GONE);		
		view.findViewById(R.id.label_regchepwd).setVisibility(View.GONE);
		view.findViewById(R.id.register_chepwd).setVisibility(View.GONE);	
		view.findViewById(R.id.lable_regtype).setVisibility(View.GONE);
		view.findViewById(R.id.register_type).setVisibility(View.GONE);
		
		
//		view.findViewById(R.id.register_account).setVisibility(View.GONE);
//		view.findViewById(R.id.register_pwd1).setVisibility(View.GONE);
//		view.findViewById(R.id.register_pwd2).setVisibility(View.GONE); 
//		view.findViewById(R.id.register_verifi).setVisibility(View.GONE);
//		view.findViewById(R.id.register_cut).setVisibility(View.GONE);

		view.findViewById(R.id.register_cut2).setVisibility(View.GONE);
		view.findViewById(R.id.register_cut3).setVisibility(View.GONE);
		view.findViewById(R.id.register_cut4).setVisibility(View.GONE);
		view.findViewById(R.id.register_cut8).setVisibility(View.GONE);		
		view.findViewById(R.id.register_tel_click)
				.setVisibility(View.INVISIBLE);
		person = (ImageView) view.findViewById(R.id.person_img);
		company = (ImageView) view.findViewById(R.id.company_img);
		title = (TextView) view.findViewById(R.id.register_title);
		commit = (TextView) view.findViewById(R.id.register_commit);
		title.setText("设置");
		commit.setText("修改设置");
		person.setOnClickListener(this);
		company.setOnClickListener(this);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
		view.findViewById(R.id.register_btn_lay).setOnClickListener(this);
	}

	void setText() {
		if (Util.userlist.size() != 0) {
			name.setText(Util.userlist.get(0).substring(1));
			nick.setText(Util.userlist.get(1).substring(1));
			email.setText(Util.userlist.get(2).substring(1));
			phone.setText(Util.userlist.get(4).substring(1));
			phone.setTextColor(Color.parseColor("#929292"));
			phone.setEnabled(false);
			if (Util.userlist.get(3).equals("40")) {
				userType = "40";
				person.performClick();
			} else {
				userType = "41";
				company.performClick();
			}
		}
	}

	boolean commitTxt() {
		redto.username = name.getText().toString();
		redto.nickName = nick.getText().toString();
		// redto.mobile = phone.getText().toString();
		redto.email = email.getText().toString();
		redto.userType = userType.substring(1, 2);
		redto.province = Util.idpro;
		redto.city = Util.idcity;
		if (!CheckList2.check(Util.userlist, redto)) {
			setInfo(redto);
			Util.showMsg(this.getActivity(), "修改信息成功！");
		} else {
			Util.showMsg(this.getActivity(), "信息未修改！");
			return false;
		}

		return true;
	}

	void setInfo(SettingDto dto) {
		Util.userlist.set(1, "2" + dto.nickName);
		Util.userlist.set(2, "3" + dto.email);
		Util.userlist.set(6, "7" + dto.province);
		Util.userlist.set(7, "8" + dto.city);
		SaveUserInfo.putInfo(new TreeSet<String>(Util.userlist));
	}

	void setNet() {
		SettingDto repho = (SettingDto) NetWork.NetResult(
				"auth/modifyPersonInfo/" + Util.params[0] + Util.params[1],
				SettingDto.class, redto);
		if (repho != null) {
			if (repho.errorCode.equals("0")) {
				Util.closeProssbar();
				Jpage(new MineFragment());
			} else {
				Util.showMsg(this.getActivity(), repho.errorMsg);
				Util.closeProssbar();
			}
		} else {
			Util.showMsg(this.getActivity(), "服务器或网络异常！");
			Util.closeProssbar();
		}
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
				int Pid = 0;
				int Cid = 0;
				setText();
				if (Util.userlist.size() > 7) {
					Pid = Integer.valueOf(Util.userlist.get(6).substring(1));
					Cid = Integer.valueOf(Util.userlist.get(7).substring(1));
				}
				arealay.addView(ChooseAreaView.oncreat(inflater,
						SettingFragment.this.getActivity(), Pid, Cid));
				Util.closeProssbar();
				break;

			case 1:
				if (commitTxt())
					setNet();
				else {
					Util.closeProssbar();
					Jpage(new MineFragment());
				}
				break;

			default:
				break;
			}
		};
	};
}
