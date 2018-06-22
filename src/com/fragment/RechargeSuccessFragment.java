package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.base.BaseActivity;
import com.lease.R;
import com.main.MainActivity;
import com.util.Util;

public class RechargeSuccessFragment extends BaseActivity implements
		OnClickListener {
	View view;
	Message msg;
	TextView txt, cash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Util.fragmentTag = this.toString().split("[{]")[0];
		setContentView(R.layout.recharge_success_fragment);

		init();
	}

	void init() {
		txt = (TextView) findViewById(R.id.success_txt);
		cash = (TextView) findViewById(R.id.success_cash);
		if (!Util.recharType.equals("") && !Util.recharCash.equals("")) {
			txt.setText(Util.recharType);
			cash.setText(Util.recharCash);
		}
		findViewById(R.id.recharge_success_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_success_btn:
			Util.mainTag = true;
			startActivity(new Intent(RechargeSuccessFragment.this,
					MainActivity.class));
			finish();
			break;

		default:
			break;
		}
	}
}
