package com.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

import com.base.BaseActivity;
import com.lease.R;
import com.util.Util;

public class WritePwdFragment extends BaseActivity implements OnClickListener,
		OnKeyListener {
	Message msg;
	EditText edit;
	TextView txt, value;
	View view1, view2, view3, view4, view5, view6, backbtn, keyview;
	List<String> pwdStr = new ArrayList<String>();
	View[] viewlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Util.fragmentTag = this.toString().split("[{]")[0];
		setContentView(R.layout.dialog_writepwd);
		keyview = getWindow().peekDecorView();

		init();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.write_btn:
			finish();
			break;

		default:
			break;
		}
	}

	TextWatcher mTextWatcher = new TextWatcher() {
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
			int i = edit.getText().length();
			for (int j = 0; j < i; j++) {
				viewlist[j].setVisibility(View.VISIBLE);
				if (j == 5) {
					Util.showProssbar(WritePwdFragment.this);
					creatMsg(0, 0);
				}
			}
		}
	};

	void init() {
		txt = (TextView) findViewById(R.id.writepwd_txt);
		value = (TextView) findViewById(R.id.writepwd_value);
		txt.setText(Util.recharType);
		value.setText("￥" + Util.recharCash);
		edit = (EditText) findViewById(R.id.write_ed);
		backbtn = findViewById(R.id.write_btn);
		edit.addTextChangedListener(mTextWatcher);
		edit.setOnKeyListener(this);
		backbtn.setOnClickListener(this);
		view1 = findViewById(R.id.img1);
		view2 = findViewById(R.id.img2);
		view3 = findViewById(R.id.img3);
		view4 = findViewById(R.id.img4);
		view5 = findViewById(R.id.img5);
		view6 = findViewById(R.id.img6);
		View[] list = { view1, view2, view3, view4, view5, view6, };
		viewlist = list;
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
				pwdStr.add(edit.getText().toString());
				Util.hideKeyboard(keyview, WritePwdFragment.this);
				startActivity(new Intent(WritePwdFragment.this,
						RechargeSuccessFragment.class));
				Util.closeProssbar();
				finish();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DEL
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			pwdStr.clear();
			edit.setText("");
			for (View img : viewlist) {
				img.setVisibility(View.GONE);
			}
			return true;
		}
		return false;
	}
}
