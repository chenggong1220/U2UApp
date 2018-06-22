package com.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fragment.AskForFragment;
import com.lease.R;
import com.util.MyApplication;
import com.util.Util;

public class AskForAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<Map<String, String>> map;
	int[] vallist = new int[Util.choosemap.size()];

	public AskForAdapter(List<Map<String, String>> map) {
		this.map = map;
	}

	@Override
	public int getCount() {
		return map.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.item_askfor_list, null);
			viewHolder = new ViewHolder();
			viewHolder.mins = (ImageView) convertView
					.findViewById(R.id.ask_mins);
			viewHolder.plus = (ImageView) convertView
					.findViewById(R.id.ask_plus);
			viewHolder.num = (TextView) convertView.findViewById(R.id.ask_num);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.ask_name);
			viewHolder.meal = (TextView) convertView
					.findViewById(R.id.ask_meal);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final String model = map.get(position).get(Util.HOME_DEVICE[0]);
		viewHolder.name.setText(model);
		viewHolder.meal.setText(map.get(position).get(Util.HOME_DEVICE[1]));
		final TextView showNum = viewHolder.num;
		// showNum.setText("" + i);
		viewHolder.mins.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.isFastClick())
					return;
				int i = vallist[position];
				if (i > 1) {
					i--;
					showNum.setText("" + i);
					AskForFragment.hasDevNum(model, i);
					vallist[position] = i;
				} else
					Util.showMsg(MyApplication.getAppContext(), "请至少选择一个设备！");
			}
		});
		viewHolder.plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.isFastClick())
					return;
				int i = vallist[position] == 0 ? 1 : vallist[position];
				i++;
				showNum.setText("" + i);
				AskForFragment.hasDevNum(model, i);
				vallist[position] = i;
			}
		});

		return convertView;
	}

	static class ViewHolder {
		public ImageView mins;
		public TextView name;
		public TextView num;
		public TextView meal;
		public ImageView plus;
	}
}
