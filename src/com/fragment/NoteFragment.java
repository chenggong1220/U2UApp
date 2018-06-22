package com.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.base.BaseFragment;
import com.dto.NoteListDto;
import com.lease.R;
import com.main.MainActivity;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.util.Util;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

public class NoteFragment extends BaseFragment implements IXListViewListener,
		OnItemClickListener {
	View view;
	Message msg;
	XListView listview;
	NoteListDto dto = new NoteListDto();
	int page = 1;
	String[] idlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.note_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		init();
		msg = new Message();
		msg.what = 0;
		handler.sendMessageDelayed(msg, 0);
		return view;
	}

	@Override
	public void onClick(View v) {

	}

	List<Map<String, String>> getData() {
		dto.pageSize = "10";
		dto.page = String.valueOf(page);
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		Object result = NetWork.NetResult("message/getMessages/", null, dto);
		if (result == null) {
			Util.showMsg(this.getActivity(), "服务器或网络异常！");
		} else {
			JSONArray list = Util.json2ary(result.toString());
			idlist = new String[list.length()];
			if (list.length() != 0) {
				view.findViewById(R.id.note_hint).setVisibility(View.GONE);
				MainActivity.hideRed();
				for (int i = 0; i < idlist.length; i++) {
					map = new HashMap<String, String>();
					try {
						JSONObject mark = (JSONObject) list.get(i);
						idlist[i] = (String) mark.getString("id");
						map = new HashMap<String, String>();
						map.put(Util.MEAL_CONTENT[0],
								(String) mark.getString("description"));
						map.put(Util.MEAL_CONTENT[1],
								(String) mark.getString("title"));
						String date = (String) mark.getString("createDate");
						map.put(Util.MEAL_CONTENT[2], date.substring(4, 6)
								+ "-" + date.substring(6, 8));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					data.add(map);
				}
			}
		}
		return data;
	}

	void init() {
		listview = (XListView) view.findViewById(R.id.note_lv);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				listview.setAdapter(new SimpleAdapter(NoteFragment.this
						.getActivity(), getData(), R.layout.item_note_list,
						Util.MEAL_CONTENT, new int[] { R.id.note_content,
								R.id.note_title, R.id.note_data }));
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};

	// 上一页
	@Override
	public void onRefresh() {
		Util.showMsg(this.getActivity(), "已经是第一页！");
		listview.stopRefresh();
	}

	// 下一页
	@Override
	public void onLoadMore() {
		Util.showMsg(this.getActivity(), "已经没有更多！");
		listview.stopLoadMore();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Util.id = idlist[position - 1];
		Jpage(new NoteDetailFragment());
	}
}
