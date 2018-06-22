package com.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.base.BaseActivity;
import com.dto.LoginDto;
import com.dto.ReLoginDto;
import com.fragment.FindPwdActivity;
import com.lease.R;
import com.main.MainActivity;
import com.net.NetWork;
import com.register.RegisterActivity;
import com.tools.SaveUserInfo;
import com.util.Util;

public class LoginActivity extends BaseActivity implements OnClickListener {
	EditText User, Pass;
	View keyview;
	Message msg;
	String name, pwd, regID;
	ReLoginDto dto = new ReLoginDto();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		keyview = getWindow().peekDecorView();

		init();
	}

	void init() {
		User = (EditText) findViewById(R.id.login_user);
		Pass = (EditText) findViewById(R.id.login_pass);
		findViewById(R.id.findpwd).setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
		findViewById(R.id.login_lay).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_lay:
			Util.showProssbar(this);
			Util.hideKeyboard(keyview, this);
			name = User.getText().toString();
			pwd = Pass.getText().toString();
			if (name.equals("") || pwd.equals("")) {
				Util.closeProssbar();
				Util.showMsg(this, "请输入用户名密码！");
				return;
			}
			dto.username = name;
			dto.pwd = pwd;
			dto.regID = getDeviceRegID();
			creatMsg(0, 100);
			break;

		case R.id.register:
			Util.showProssbar(this);
			creatMsg(1, 0);
			break;

		case R.id.back_btn:
			finish();
			break;

		case R.id.findpwd:
			Util.showProssbar(this);
			creatMsg(2, 0);
			break;

		default:
			break;
		}
	}

	Set<String> bean2set(ReLoginDto dto) {
		List<String> list = new ArrayList<String>();
		list.add(1 + dto.username);
		list.add(2 + dto.nickname);
		list.add(3 + dto.email);
		list.add(4 + dto.userType);
		list.add(5 + dto.mobile);
		list.add(6 + dto.pushMessage);
		list.add(7 + dto.provinceId);
		list.add(8 + dto.cityId);
		return new TreeSet<String>(list);
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
				LoginDto redto = (LoginDto) NetWork.NetResult("auth/login",
						LoginDto.class, dto);
				if (redto != null) {
					if (redto.errorCode.equals("0")) {
						SaveUserInfo.putUser(name, "/" + pwd);
						SaveUserInfo.putInfo(bean2set(redto.message));
						MainActivity.chanUser();
						LoginActivity.this.finish();
						//Util.showMsg(LoginActivity.this, "欢迎你！" + dto.nickname);
					} else
						Util.showMsg(LoginActivity.this, redto.errorMsg);
				}
				Util.closeProssbar();
				break;

			case 1:
				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));
				Util.closeProssbar();
				break;

			case 2:
				startActivity(new Intent(LoginActivity.this,
						FindPwdActivity.class));
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!Util.curUserID.equals("")){
			//注册成功后将注册ID带回到登录页面
			User.setText(Util.curUserID);
		}
	}
	
	private String getDeviceRegID(){
		String rid = JPushInterface.getRegistrationID(getApplicationContext());
		if (!rid.isEmpty()) {
			return rid;
		}else{
			return null;
		}
	}
}
