package com.register;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dto.PhoneDto;
import com.dto.RegisterDto;
import com.lease.R;
import com.net.NetWork;
import com.tools.ChooseAreaView;
import com.util.Util;

public class RegisterActivity extends Activity implements OnClickListener {
	ImageView person, company;
	PhoneDto dto = new PhoneDto();
	RegisterDto redto = new RegisterDto();
	TextView click;
	LinearLayout arealay;
	EditText name, nick, pwd, chepwd, sms, email, phone;
	Message msg;
	String tel;
	String usetType = "0";
	String SMS_CONTENT = "获取验证码";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);

		init();
		click.setOnClickListener(this);
		person.setOnClickListener(this);
		company.setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.register_btn_lay).setOnClickListener(this);
	}

	void init() {
		name = (EditText) findViewById(R.id.register_name);
		nick = (EditText) findViewById(R.id.register_nick);
		pwd = (EditText) findViewById(R.id.register_pwd);
		chepwd = (EditText) findViewById(R.id.register_chepwd);
		sms = (EditText) findViewById(R.id.register_sms);
		email = (EditText) findViewById(R.id.register_email);
		phone = (EditText) findViewById(R.id.register_phone);
		phone.addTextChangedListener(textwatcher);
		click = (TextView) findViewById(R.id.register_tel_click);
		person = (ImageView) findViewById(R.id.person_img);
		company = (ImageView) findViewById(R.id.company_img);
		arealay = (LinearLayout) findViewById(R.id.register_area_lay);
		arealay.addView(ChooseAreaView.oncreat(LayoutInflater.from(this), this));
	}

	TextWatcher textwatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if ((s.toString()).equals(phone.getText().toString()))
				click.setText(SMS_CONTENT);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;

		case R.id.person_img:
			usetType = "0";
			person.setBackgroundResource(R.drawable.register_check);
			company.setBackgroundResource(R.drawable.register_uncheck);
			break;

		case R.id.company_img:
			usetType = "1";
			person.setBackgroundResource(R.drawable.register_uncheck);
			company.setBackgroundResource(R.drawable.register_check);
			break;

		case R.id.register_tel_click:
			if (!click.getText().equals(SMS_CONTENT))
				return;
			tel = phone.getText().toString();
			if (!Util.isMobileNo(tel))
				Util.showMsg(this, "请输入有效手机号码！");
			else {
				// click.setText("已发送");
				counttimer.start();
				dto.mobile = tel;
				creatMsg(0);
			}
			break;

		case R.id.register_btn_lay:
			if (checkInfo()) {
				Util.showProssbar(this);
				creatMsg(1);
			}
			break;

		default:
			break;
		}
	}

	boolean checkInfo() {
		String mobile = phone.getText().toString();
		String use = mobile;						//name.getText().toString();	取消用用户自定义ID作为登录ID,用手机号码作为ID
		String nkuse = nick.getText().toString();
		String code = sms.getText().toString();
		String eml = email.getText().toString();
		String p1 = pwd.getText().toString();
		String p2 = chepwd.getText().toString();

		if (!Util.isMobileNo(mobile))
		{
			Util.showMsg(this, "请输入有效的手机号码！");
			return false;
		}
		if (code.equals("")){
			Util.showMsg(this, "请输入手机验证码！");
			return false;
		}		
		if (p1.equals("")){
			Util.showMsg(this, "请设置您的登录密码！");
			return false;		
		}
		if(!p1.equals(p2)){
			Util.showMsg(this, "两次密码输入不一致！");
			return false;
		}
		
		if(nkuse.equals("")){
			Util.showMsg(this, "请输入昵称！");
			return false;
		}		
		if (eml.equals("") || !Util.isEmail(eml)){
			Util.showMsg(this, "请输入有效EMail账号！");
			return false;
		}


		redto.city = Util.idcity;
		redto.email = eml;
		redto.mobile = tel;
		redto.nickname = nkuse;
		redto.province = Util.idpro;
		redto.pwd = p1;
		redto.smsCode = code;
		redto.username = tel;
		redto.userType = usetType;

		return true;
	}

	void creatMsg(int what) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, 0);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				PhoneDto repho = (PhoneDto) NetWork.NetResult(
						"auth/getSmsCode", PhoneDto.class, dto);
				if (repho != null) {
					if (repho.errorCode.equals("0"))
						Util.showMsg(RegisterActivity.this, "短信发送成功！");
					else
						Util.showMsg(RegisterActivity.this, "短信发送失败！");
				} else
					Util.showMsg(RegisterActivity.this, "服务器或网络异常！");
				break;

			case 1:
				RegisterDto remsg = (RegisterDto) NetWork.NetResult(
						"auth/register", RegisterDto.class, redto);
				if (remsg != null) {
					if (remsg.errorCode.equals("0")) {
						Util.curUserID = redto.mobile;
						RegisterActivity.this.finish();
					}
					Util.showMsg(RegisterActivity.this, remsg.errorMsg);
				} else
					Util.showMsg(RegisterActivity.this, "服务器或网络异常！");
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};

	// 定时器
	CountDownTimer counttimer = new CountDownTimer(60000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			click.setText((millisUntilFinished / 1000) + "秒后可重发");
			click.setEnabled(false);
		}

		@Override
		public void onFinish() {
			click.setEnabled(true);
			click.setText(SMS_CONTENT);
		}
	};
}
