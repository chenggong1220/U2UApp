package com.fragment;

import java.util.regex.Pattern;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.base.BaseActivity;
import com.dto.FindPwdDto;
import com.dto.PhoneDto;
import com.lease.R;
import com.net.NetWork;
import com.util.Util;

public class FindPwdActivity extends BaseActivity implements OnClickListener {
	Message msg;
	EditText user, edt1, edt2;
	TextView code, txt1, txt2;
	String phone, yanz;
	boolean isIn;
	PhoneDto phodto = new PhoneDto();
	FindPwdDto dto = new FindPwdDto();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.find_pwd_activity);
		init();
	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	void init() {
		edt1 = (EditText) findViewById(R.id.pwd_old);
		edt2 = (EditText) findViewById(R.id.pwd_new);
		user = (EditText) findViewById(R.id.pwd_user);
		txt1 = (TextView) findViewById(R.id.find_pwd_txt1);
		txt2 = (TextView) findViewById(R.id.find_pwd_txt2);
		code = (TextView) findViewById(R.id.find_pwd_code);
		code.setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.find_pwd_commit).setOnClickListener(this);
	}

	boolean check() {
		yanz = edt2.getText().toString();
		if (yanz.equals("")) {
			Util.showMsg(this, "请填写验证码！");
			return false;
		}
		return true;
	}

	boolean check2() {
		String uname = user.getText().toString();
		String pwd1 = edt1.getText().toString();
		String pwd2 = edt2.getText().toString();
		if (pwd1.equals("") || pwd2.equals("") || uname.equals("")) {
			Util.showMsg(this, "请填写完整信息！");
			return false;
		}
		if (!pwd1.equals(pwd2)) {
			Util.showMsg(this, "两次密码不一致！");
			return false;
		}
		dto.username = uname;
		dto.password = pwd2;
		dto.mobile = phone;
		return true;
	}

	boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			if (!isIn)
				finish();
			else {
				isIn = false;
				setTxt("手机号码 :", "验证码 :", "b");
			}
			break;

		case R.id.find_pwd_code:
			phone = edt1.getText().toString();
			if (!phone.equals("") && phone.length() == 11 && isNumeric(phone)) {
				phodto.mobile = phone;
				code.setText("已发送");
				creatMsg(2, 0);
			} else
				Util.showMsg(this, "请填写合法手机号！");
			break;

		case R.id.find_pwd_commit:
			Util.showProssbar(this);
			if (!isIn) {
				if (check()) {
					isIn = true;
					creatMsg(0, 0);
				} else
					Util.closeProssbar();
			} else {
				if (check2())
					creatMsg(1, 0);
				else
					Util.closeProssbar();
			}
			break;

		default:
			break;
		}
	}

	void setTxt(String... parm) {
		edt1.setText("");
		user.setText("");
		user.requestFocus();
		txt1.setText(parm[0]);
		txt2.setText(parm[1]);
		if (parm[2].equals("a")) {
			isIn = true;
			findViewById(R.id.pwd_lay).setVisibility(View.VISIBLE);
			edt1.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			edt2.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			code.setVisibility(View.INVISIBLE);
		} else {
			isIn = false;
			findViewById(R.id.pwd_lay).setVisibility(View.GONE);
			edt1.setText(phone);
			edt1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			edt2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			code.setVisibility(View.VISIBLE);
		}
		edt2.setText("");
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dto.smsCode = yanz;
				setTxt("新的密码 :", "确认密码 :", "a");
				Util.closeProssbar();
				break;

			case 1:
				FindPwdDto redto = (FindPwdDto) NetWork.NetResult(
						"auth/resetLoginPwd", FindPwdDto.class, dto);
				if (redto != null) {
					if (redto.errorCode.equals("0")) {
						Util.closeProssbar();
						creatMsg(3, 0);
					} else
						Util.showMsg(FindPwdActivity.this, redto.errorMsg);
				} else
					Util.showMsg(FindPwdActivity.this, "服务器或网络异常！");
				Util.closeProssbar();
				break;

			case 2:
				PhoneDto repho = (PhoneDto) NetWork.NetResult(
						"auth/getSmsCode", PhoneDto.class, phodto);
				if (repho != null) {
					if (repho.errorCode.equals("0"))
						Util.showMsg(FindPwdActivity.this, "短信发送成功！");
					else
						Util.showMsg(FindPwdActivity.this, "短信发送失败！");
				} else
					Util.showMsg(FindPwdActivity.this, "服务器或网络异常！");
				break;

			case 3:
				finish();
				Util.showMsg(FindPwdActivity.this, "密码重置成功！");
				break;

			default:
				break;
			}
		};
	};
}
