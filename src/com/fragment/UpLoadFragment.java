package com.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseFragment;
import com.dto.ReImageDto;
import com.dto.UploadDto;
import com.lease.R;
import com.net.Config;
import com.net.NetWork;
import com.tools.SaveUserInfo;
import com.tools.SelectPicActivity;
import com.tools.UploadUtil;
import com.util.Util;

public class UpLoadFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;
	LinearLayout frontlay, backlay, certifilay, fronthintlay, backhintlay, certifihintlay;
	// MESSI
	String userType = "0";
	LinearLayout certification_gone;
	static RelativeLayout refrontlay, rebacklay, recertifilay;
	static TextView fronttxt, backtxt, certifitxt, frontdel, backdel, certifidel;
	ImageView front, back, certifi;
	EditText username, idcard;
	Bitmap bm;
	String picPath;
	String type;
	String requestURL = Config.Url + "common/statics/upload";
	BitmapFactory.Options options = new BitmapFactory.Options();
	UploadDto dto = new UploadDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.upload_fragment, null);

		init();
		getUserMsg();
		creatMsg(0, 0);
		return view;
	}

	void init() {
		frontdel = (TextView) view.findViewById(R.id.front_deltxt);
		backdel = (TextView) view.findViewById(R.id.back_deltxt);
		certifidel = (TextView) view.findViewById(R.id.certificationPicture_deltxt);
		fronthintlay = (LinearLayout) view.findViewById(R.id.front_hintlay);
		backhintlay = (LinearLayout) view.findViewById(R.id.back_hintlay);
		certifihintlay = (LinearLayout) view.findViewById(R.id.certificationPicture_hintlay);
		username = (EditText) view.findViewById(R.id.upload_name);
		idcard = (EditText) view.findViewById(R.id.upload_idcard);
		fronttxt = (TextView) view.findViewById(R.id.front_retxt);
		backtxt = (TextView) view.findViewById(R.id.back_retxt);
		refrontlay = (RelativeLayout) view.findViewById(R.id.front_relay);
		rebacklay = (RelativeLayout) view.findViewById(R.id.back_relay);
		recertifilay = (RelativeLayout) view.findViewById(R.id.certificationPicture_relay);
		certifitxt = (TextView) view.findViewById(R.id.certificationPicture_retxt);
		frontlay = (LinearLayout) view.findViewById(R.id.upload_front_lay);
		backlay = (LinearLayout) view.findViewById(R.id.upload_back_lay);
		certifilay = (LinearLayout) view.findViewById(R.id.upload_certificationPicture_lay);
		front = (ImageView) view.findViewById(R.id.upload_front);
		back = (ImageView) view.findViewById(R.id.upload_back);
		certifi = (ImageView) view.findViewById(R.id.upload_certificationPicture);
		// MESSI
		certification_gone = (LinearLayout) view.findViewById(R.id.certification_gone);
		frontdel.setOnClickListener(this);
		backdel.setOnClickListener(this);
		certifidel.setOnClickListener(this);
		fronttxt.setOnClickListener(this);
		backtxt.setOnClickListener(this);
		certifitxt.setOnClickListener(this);
		frontlay.setOnClickListener(this);
		backlay.setOnClickListener(this);
		// 营业执照
		certifilay.setOnClickListener(this);
		front.setOnClickListener(this);
		back.setOnClickListener(this);
		certifi.setOnClickListener(this);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
		view.findViewById(R.id.register_btn_lay).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new MineFragment());
			break;

		case R.id.upload_front_lay:
			type = "front";
			startActivityForResult(new Intent(this.getActivity(), SelectPicActivity.class), 3);
			break;

		case R.id.upload_back_lay:
			if (!fronttxt.getText().equals("完成")) {
				Util.showMsg(this.getActivity(), "请先上传身份证正面照！");
				return;
			}
			type = "back";
			startActivityForResult(new Intent(this.getActivity(), SelectPicActivity.class), 3);
			break;

		case R.id.upload_certificationPicture_lay:
			if (!backtxt.getText().equals("完成")) {
				Util.showMsg(this.getActivity(), "请先上传身份证反面照！");
				return;
			}
			type = "certificationPicture";
			startActivityForResult(new Intent(this.getActivity(), SelectPicActivity.class), 3);
			break;

		case R.id.front_retxt:
			if (fronttxt.getText().equals("完成"))
				return;
			if (picPath != null) {
				UploadUtil.uploadFile(picPath, "pic", requestURL, this.getActivity(), type);
			}
			break;

		case R.id.back_retxt:
			if (backtxt.getText().equals("完成"))
				return;
			if (picPath != null) {
				UploadUtil.uploadFile(picPath, "pic", requestURL, this.getActivity(), type);
			}
			break;

		case R.id.certificationPicture_retxt:
			if (certifitxt.getText().equals("完成"))
				return;
			if (picPath != null) {
				UploadUtil.uploadFile(picPath, "pic", requestURL, this.getActivity(), type);
			}
			break;

		case R.id.upload_front:
			fronthintlay.setVisibility(View.VISIBLE);
			break;

		case R.id.upload_back:
			backhintlay.setVisibility(View.VISIBLE);
			break;

		case R.id.upload_certificationPicture:
			certifihintlay.setVisibility(View.VISIBLE);
			break;

		case R.id.front_deltxt:
			front.setImageBitmap(null);
			fronttxt.setText("上传");
			refrontlay.setVisibility(View.GONE);
			fronthintlay.setVisibility(View.GONE);
			frontlay.setVisibility(View.VISIBLE);
			break;

		case R.id.back_deltxt:
			back.setImageBitmap(null);
			backtxt.setText("上传");
			rebacklay.setVisibility(View.GONE);
			backhintlay.setVisibility(View.GONE);
			backlay.setVisibility(View.VISIBLE);
			break;

		case R.id.certificationPicture_deltxt:
			certifi.setImageBitmap(null);
			certifitxt.setText("上传");
			recertifilay.setVisibility(View.GONE);
			certifihintlay.setVisibility(View.GONE);
			certifilay.setVisibility(View.VISIBLE);
			break;

		case R.id.register_btn_lay:
			if ("0".equals(userType)) {
				if (check2()) {
					Util.showMsg(this.getActivity(), "请填写完整信息！");
					return;
				}
				Util.showProssbar(this.getActivity());
				dto.realName = username.getText().toString();
				dto.idCard = idcard.getText().toString();
				dto.front = Util.imgPath.get("front");
				dto.back = Util.imgPath.get("back");
				// dto.certificationPicture =
				// Util.imgPath.get("certificationPicture");
				creatMsg(1, 0);
			} else {
				if (check()) {
					Util.showMsg(this.getActivity(), "请填写完整信息！");
					return;
				}
				Util.showProssbar(this.getActivity());
				dto.realName = username.getText().toString();
				dto.idCard = idcard.getText().toString();
				dto.front = Util.imgPath.get("front");
				dto.back = Util.imgPath.get("back");
				 dto.certificationPicture =
				 Util.imgPath.get("certificationPicture");
				creatMsg(1, 0);
			}

			// Util.showProssbar(this.getActivity());
			// dto.realName = username.getText().toString();
			// dto.idCard = idcard.getText().toString();
			// dto.front = Util.imgPath.get("front");
			// dto.back = Util.imgPath.get("back");
			// dto.certificationPicture =
			// Util.imgPath.get("certificationPicture");
			// creatMsg(1, 0);
			break;

		default:
			break;
		}
	}

	boolean check() {
		if (username.getText().toString().equals("") || idcard.getText().toString().equals("") || idcard.getText().toString().length() < 18
				|| fronttxt.getText().equals("上传") || backtxt.getText().equals("上传") || certifitxt.getText().equals("上传"))
			return true;
		else
			return false;
	}

	boolean check2() {
		if (username.getText().toString().equals("") || idcard.getText().toString().equals("") || idcard.getText().toString().length() < 18
				|| fronttxt.getText().equals("上传") || backtxt.getText().equals("上传"))
			return true;
		else
			return false;
	}

	public static void Okup() {
		if (Util.imgPath.containsKey("front") && refrontlay != null) {
			refrontlay.setEnabled(false);
			fronttxt.setText("完成");
			frontdel.setVisibility(View.INVISIBLE);
		}
		if (Util.imgPath.containsKey("back") && rebacklay != null) {
			rebacklay.setEnabled(false);
			backtxt.setText("完成");
			backdel.setVisibility(View.INVISIBLE);
		}
		if (Util.imgPath.containsKey("certificationPicture") && recertifilay != null) {
			recertifilay.setEnabled(false);
			certifitxt.setText("完成");
			certifidel.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 3) {
			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			options.inSampleSize = 10;
			bm = BitmapFactory.decodeFile(picPath, options);
			setImg();
		}
	}

	void setImg() {
		if (type.equals("front")) {
			refrontlay.setVisibility(View.VISIBLE);
			frontlay.setVisibility(View.GONE);
			front.setImageBitmap(bm);
			Util.showMsg(this.getActivity(), "再次点击图片可上传！");
		} else if (type.equals("back")) {
			rebacklay.setVisibility(View.VISIBLE);
			backlay.setVisibility(View.GONE);
			back.setImageBitmap(bm);
			Util.showMsg(this.getActivity(), "再次点击图片可上传！");
		} else {
			recertifilay.setVisibility(View.VISIBLE);
			certifilay.setVisibility(View.GONE);
			certifi.setImageBitmap(bm);
			Util.showMsg(this.getActivity(), "再次点击图片可上传！");
		}
	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	// MESSI
	void getUserMsg() {
		Util.userlist = SaveUserInfo.relist();
		if (Util.userlist.size() != 0) {
			userType = Util.userlist.get(3).substring(1);
			if ("0".equals(userType)) {
				certification_gone.setVisibility(View.INVISIBLE);
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Util.closeProssbar();
				break;

			case 1:
				ReImageDto redto = (ReImageDto) NetWork
						.NetResult("auth/uploadPersonVerify/" + Util.params[0] + Util.params[1], ReImageDto.class, dto);
				if (redto != null) {
					if (redto.errorCode.equals("0")) {
						Util.closeProssbar();
						Util.uptype = "已上传";
						Jpage(new MineFragment());
					} else {
						Util.showMsg(UpLoadFragment.this.getActivity(), redto.errorMsg);
						Util.closeProssbar();
					}
				} else {
					Util.showMsg(UpLoadFragment.this.getActivity(), "服务器或网络异常！");
					Util.closeProssbar();
				}
				break;

			default:
				break;
			}
		};
	};
}
