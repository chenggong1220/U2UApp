package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.base.BaseFragment;
import com.dto.ChangePwdDto;
import com.lease.R;
import com.net.NetWork;
import com.util.Util;

public class ChangePwdFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;
	EditText old_edt, novel, novel_sure;
	String oldpwd, newpwd;
	ChangePwdDto dto = new ChangePwdDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.change_pwd_fragment, null);

		init();
		creatMsg(0, 0);
		return view;
	}

	void init() {
		old_edt = (EditText) view.findViewById(R.id.pwd_old);
		novel = (EditText) view.findViewById(R.id.pwd_new);
		novel_sure = (EditText) view.findViewById(R.id.pwd_new_sure);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
		view.findViewById(R.id.chang_pwd_commit).setOnClickListener(this);
	}

	boolean check() {
		oldpwd = old_edt.getText().toString();
		newpwd = novel.getText().toString();
		String sure = novel_sure.getText().toString();
		if (oldpwd.equals("") || newpwd.equals("") || sure.equals("")) {
			Util.showMsg(this.getActivity(), "请输入完整信息！");
			return false;
		}
		if (!newpwd.equals(sure)) {
			Util.showMsg(this.getActivity(), "新密码输入不一致！");
			return false;
		}
		if (sure.equals(oldpwd)) {
			Util.showMsg(this.getActivity(), "新密码不能与原密码一致！");
			return false;
		}
		return true;
	}

	void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new WholeSettingFragment());
			break;

		case R.id.chang_pwd_commit:
			Util.showProssbar(this.getActivity());
			if (check()) {
				dto.oldPassword = oldpwd;
				dto.newPassword = newpwd;
				creatMsg(1, 0);
			} else
				Util.closeProssbar();
			break;

		default:
			break;
		}
	}

	boolean network() {
		ChangePwdDto repho = (ChangePwdDto) NetWork.NetResult(
				"auth/modifyLoginPwd/" + Util.params[0] + Util.params[1],
				ChangePwdDto.class, dto);
		if (repho != null) {
			if (!repho.errorCode.equals("0")) {
				Util.showMsg(this.getActivity(), repho.errorMsg);
				return false;
			}
		} else {
			Util.showMsg(this.getActivity(), "服务器或网络异常！");
			return false;
		}

		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Util.closeProssbar();
				break;

			case 1:
				if (network()) {
					Util.slidingTag = -1;
					Util.closeProssbar();
					creatMsg(2, 0);
				} else
					Util.closeProssbar();
				break;

			case 2:
				Jpage(new IndexFragment());
				Util.showMsg(ChangePwdFragment.this.getActivity(), "密码修改成功！");
				break;

			default:
				break;
			}
		};
	};
}
