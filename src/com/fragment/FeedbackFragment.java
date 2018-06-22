package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.base.BaseFragment;
import com.dto.FeedBackDto;
import com.lease.R;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.util.Util;

public class FeedbackFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;
	EditText phone, content;
	TextView commit;
	String tell, tent;
	FeedBackDto dto = new FeedBackDto();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.slidingTag = 2;
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.feedback_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		init();
		creatMsg(0, 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_commit:
			if (check()) {
				commit.setText("提交中...");
				creatMsg(1, 0);
			}
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

	boolean check() {
		tell = phone.getText().toString();
		tent = content.getText().toString();
		if ((tell.equals("") || tell.length() < 11) || tent.equals("")) {
			Util.showMsg(this.getActivity(), "请填写完整信息！");
			return false;
		} else {
			dto.phone = tell;
			dto.description = tent;
			return true;
		}
	}

	void init() {
		commit = (TextView) view.findViewById(R.id.register_commit);
		phone = (EditText) view.findViewById(R.id.feedback_phone);
		content = (EditText) view.findViewById(R.id.feedback_content);
		view.findViewById(R.id.feedback_commit).setOnClickListener(this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Util.closeProssbar();
				break;

			case 1:
				FeedBackDto redto = (FeedBackDto) NetWork.NetResult(
						"message/feedback/" + Util.params[0] + Util.params[1],
						FeedBackDto.class, dto);
				if (redto != null) {
					if (redto.errorCode.equals("0")) {
						Util.slidingTag = -1;
						Util.showMsg(FeedbackFragment.this.getActivity(),
								"感谢您的意见，我们会尽快处理！");
						Jpage(new IndexFragment());
					} else {
						commit.setText("提交");
						Util.showMsg(FeedbackFragment.this.getActivity(),
								redto.errorMsg);
					}
				} else {
					commit.setText("提交");
					Util.showMsg(FeedbackFragment.this.getActivity(),
							"服务器或网络异常！");
				}

			default:
				break;
			}
		};
	};

}
