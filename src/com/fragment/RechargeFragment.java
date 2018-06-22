package com.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.dto.AlipayInDto;
import com.dto.AplayDto;
import com.dto.ReAplayDto;
import com.lease.R;
import com.net.NetWork;
import com.pay.PayUtil;
import com.util.Util;

public class RechargeFragment extends BaseFragment implements OnClickListener {
	View view;
	EditText cash;
	Message msg;
	TextView typeTxt;
	Dialog dialog;
	LayoutInflater inflater;
	RadioGroup radiogroup;
	RadioButton btn1, btn2, btn3;
	String type;
	static TextView btntxt;
	AplayDto dto = new AplayDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.recharge_fragment, null);

		init();
		creatMsg(0, 0);

		return view;
	}

	void init() {
		btntxt = (TextView) view.findViewById(R.id.recharge_btn_txt);
		cash = (EditText) view.findViewById(R.id.recharge_cash);
		typeTxt = (TextView) view.findViewById(R.id.recharge_type_txt);
		typeTxt.setOnClickListener(this);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
		view.findViewById(R.id.recharge_view).setOnClickListener(this);
		view.findViewById(R.id.recharge_choose).setOnClickListener(this);
		view.findViewById(R.id.recharge_btn_lay).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new MyWalletFragment());
			break;

		case R.id.recharge_btn_lay:
			Util.showProssbar(this.getActivity());
			Util.recharType = typeTxt.getText().toString();
			Util.recharCash = cash.getText().toString();
			creatMsg(1, 0);
			// startActivity(new Intent(this.getActivity(),
			// WritePwdFragment.class));
			break;

		case R.id.recharge_choose:
			showdialog();
			break;

		case R.id.recharge_type_txt:
			showdialog();
			break;

		case R.id.recharge_view:
			showdialog();
			break;

		case R.id.recharge_cancle:
			if (dialog != null)
				dialog.dismiss();
			break;

		case R.id.radio1:
			typeTxt.setText("会员费");
			if (dialog != null)
				dialog.dismiss();
			break;

		case R.id.radio2:
			typeTxt.setText("保证金");
			if (dialog != null)
				dialog.dismiss();
			break;

		case R.id.radio3:
			typeTxt.setText("余额");
			if (dialog != null)
				dialog.dismiss();
			break;

		default:
			break;
		}
	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	public static void sucesse() {
		if (btntxt != null)
			btntxt.setText("再充一笔");
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Util.closeProssbar();
				break;

			case 1:
				dto.type = Util.recharType.contains("会员") ? "0" : "1";
				dto.amount = Util.recharCash;
				ReAplayDto redto = (ReAplayDto) NetWork.NetResult("pay/alipay/"
						+ Util.params[0] + Util.params[1], ReAplayDto.class,
						dto);
				if (redto != null) {
					if (redto.errorCode.equals("0")) {
						AlipayInDto cashdto = redto.message;
						PayUtil.pay(RechargeFragment.this.getActivity(),
								cashdto);
						Util.closeProssbar();
					} else {
						Util.showMsg(RechargeFragment.this.getActivity(),
								redto.errorMsg);
						Util.closeProssbar();
					}
				} else {
					Util.showMsg(RechargeFragment.this.getActivity(),
							"服务器或网络异常！");
					Util.closeProssbar();
				}
				break;

			default:
				break;
			}
		};
	};

	public void showdialog() {
		type = typeTxt.getText().toString();
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_recharge_choose, null);
		radiogroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
		btn1 = (RadioButton) radiogroup.findViewById(R.id.radio1);
		btn2 = (RadioButton) radiogroup.findViewById(R.id.radio2);
		btn3 = (RadioButton) radiogroup.findViewById(R.id.radio3);
		RadioButton[] btnlist = { btn1, btn2, btn3 };
		for (RadioButton btn : btnlist) {
			if (btn.getText().toString().contains(type))
				btn.setChecked(true);
			else
				btn.setChecked(false);
		}
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		layout.findViewById(R.id.recharge_cancle).setOnClickListener(this);
		dialog = new AlertDialog.Builder(this.getActivity()).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);
	}
}
