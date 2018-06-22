package com.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseFragment;
import com.dto.DeviceInfoDto;
import com.dto.RDeviceInfoDto;
import com.lease.R;
import com.net.Config;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.BitmapHelper;
import com.util.Util;

public class DeviceIntroduceFragment extends BaseFragment implements
		OnClickListener {
	View view;
	Message msg;
	TextView model, layout, controlMethod, driving, machinePower, brand,
			moveMethod, finishSize, cutterCount, controlSystem, mainShaftSpeed,
			deposit;
	ImageView imgview;
	String imgUrl;
	DeviceInfoDto dto = new DeviceInfoDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.introduce_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		init();
		msg = new Message();
		msg.what = 0;
		handler.sendMessageDelayed(msg, 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.introduce_commit:
			break;

		case R.id.back_btn:
			Jpage(new DeviceChooseFragment());
			break;

		default:
			break;
		}
	}

	void init() {
		imgview = (ImageView) view.findViewById(R.id.introduce_img);
		model = (TextView) view.findViewById(R.id.introduce_model);
		layout = (TextView) view.findViewById(R.id.introduce_layout);
		controlMethod = (TextView) view
				.findViewById(R.id.introduce_controlMethod);
		driving = (TextView) view.findViewById(R.id.introduce_driving);
		machinePower = (TextView) view
				.findViewById(R.id.introduce_machinePower);
		brand = (TextView) view.findViewById(R.id.introduce_brand);
		moveMethod = (TextView) view.findViewById(R.id.introduce_moveMethod);
		finishSize = (TextView) view.findViewById(R.id.introduce_finishSize);
		cutterCount = (TextView) view.findViewById(R.id.introduce_cutterCount);
		controlSystem = (TextView) view
				.findViewById(R.id.introduce_controlSystem);
		mainShaftSpeed = (TextView) view
				.findViewById(R.id.introduce_mainShaftSpeed);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
		view.findViewById(R.id.introduce_commit).setOnClickListener(this);
	}

	void getData() {
		dto.assetTypeId = Util.id;
		RDeviceInfoDto redto = (RDeviceInfoDto) NetWork.NetResult(
				"device/getOneAssetType/" + Util.params[0] + Util.params[1],
				RDeviceInfoDto.class, dto);
		if (redto != null) {
			if (redto.errorCode.equals("0")) {
				DeviceInfoDto info = redto.message;
				model.setText(info.model);
				layout.setText(info.layout);
				controlMethod.setText(info.controlMethod);
				driving.setText(info.driving);
				machinePower.setText(info.machinePower);
				brand.setText(info.brand);
				moveMethod.setText(info.moveMethod);
				finishSize.setText(info.finishSize);
				cutterCount.setText(info.cutterCount);
				controlSystem.setText(info.controlSystem);
				mainShaftSpeed.setText(info.mainShaftSpeed);
				imgUrl = info.picture;
			} else
				Util.showMsg(this.getActivity(), redto.errorMsg);
		} else
			Util.showMsg(this.getActivity(), "服务器或网络异常！");
	}

	@SuppressWarnings("unchecked")
	void loadImage() {
		try {
			Object obj = new BitmapHelper(Config.ImgUrl + imgUrl).execute()
					.get();
			if (obj != null)
				imgview.setImageBitmap((Bitmap) obj);
			else
				imgview.setBackgroundResource(R.drawable.icon_error);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getData();
				loadImage();
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};
}
