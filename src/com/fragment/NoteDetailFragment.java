package com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.dto.NoteListDto;
import com.dto.ReNoteInfoDto;
import com.lease.R;
import com.net.NetWork;
import com.util.Util;

public class NoteDetailFragment extends BaseFragment implements OnClickListener {
	View view;
	Message msg;
	NoteListDto dto = new NoteListDto();
	TextView titl, date, content;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.note_detail_fragment, null);

		init();
		creatMsg(0, 0);
		return view;
	}

	void init() {
		dto.id = Util.id;
		titl = (TextView) view.findViewById(R.id.note_detail_title);
		date = (TextView) view.findViewById(R.id.note_detail_date);
		content = (TextView) view.findViewById(R.id.note_detail_content);
		view.findViewById(R.id.back_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new NoteFragment());
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

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ReNoteInfoDto redto = (ReNoteInfoDto) NetWork.NetResult(
						"message/getSingleMessage/" + Util.params[0]
								+ Util.params[1], ReNoteInfoDto.class, dto);
				if (redto != null) {
					if (redto.errorCode.equals("0")) {
						String data = redto.message.createDate;
						titl.setText(redto.message.title);
						date.setText(data.subSequence(0, 4) + "年"
								+ data.substring(4, 6) + "月"
								+ data.substring(6, 8) + "日");
						content.setText("\u3000\u3000"
								+ redto.message.description);
					} else
						Util.showMsg(NoteDetailFragment.this.getActivity(),
								redto.errorMsg);
				} else
					Util.showMsg(NoteDetailFragment.this.getActivity(),
							"服务器或网络异常！");
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};
}
