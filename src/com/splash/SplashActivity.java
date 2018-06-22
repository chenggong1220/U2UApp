package com.splash;

import com.base.BaseActivity;
import com.dto.GetVersonDto;
import com.lease.R;
import com.main.MainActivity;
import com.net.NetWork;
import com.tools.SaveUserInfo;
import com.tools.UpgradeUtil;
import com.util.ApkUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SplashActivity extends BaseActivity implements OnClickListener {
	TextView showtxt;
	Message msg;

	// String packNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		showtxt = (TextView) findViewById(R.id.guide_wait);
		findViewById(R.id.guide_jump).setOnClickListener(this);
		findViewById(R.id.guide_again).setOnClickListener(this);
		creatMsg(2, 1000);
		// packNum = GetPackage.getPackageInfo(this);
	}	

	void getData() {

		GetVersonDto redto = (GetVersonDto) NetWork.NetResult(
				"auth/checkVersion", GetVersonDto.class, null);
			
		if (redto != null) {
			if (redto.errorCode.equals("0")) {

				//去掉加载时显示版本验证的提示，SUNZHE，2017-03-21
				//showtxt.setText("版本验证成功！");
				//creatMsg(0, 1000);
				if(!redto.message.version.equals(ApkUtil.getVersionName(this)))
				{
					UpgradeUtil.checkVersion(this);
				}else{
					startActivity(new Intent(this, MainActivity.class));
					this.finish();
				}
			} else {
				showtxt.setText("服务器或网络异常！");
				creatMsg(1, 1000);
			}
		} else {
			showtxt.setText("服务器或网络异常！");
			creatMsg(1, 1000);
		}

	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide_jump:
			//去掉版本验证的后显示“立即体验”按钮，点击才能登录，SUNZHE，2017-03-21
			startActivity(new Intent(this, MainActivity.class));
	        SaveUserInfo.setFirstOpenFlag(false);
			this.finish();
			break;

		case R.id.guide_again:
			showtxt.setText("正在加载, 请稍候...");
			showtxt.setVisibility(View.VISIBLE);		
			findViewById(R.id.guide_again).setVisibility(View.GONE);
			creatMsg(2, 5000);

			break;

		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showtxt.setVisibility(View.GONE);
				findViewById(R.id.guide_jump).setVisibility(View.VISIBLE);
				break;

			case 1:
				showtxt.setVisibility(View.GONE);
				findViewById(R.id.guide_again).setVisibility(View.VISIBLE);
				break;

			case 2:
				getData();
				break;

			default:
				break;
			}
		};
	};

	/*
	 *Moved this to Splash
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
	*/
}
