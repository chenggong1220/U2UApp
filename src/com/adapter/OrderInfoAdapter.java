package com.adapter;

import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.lease.R;
import com.util.Util;

public class OrderInfoAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int mLastPosition = -1;
	private List<Map<String, String>> list;

	public OrderInfoAdapter(List<Map<String, String>> list) {
		this.list = list;
	}

	@Override
	public int getItemViewType(int position) {
		if (position % 2 == 0) {
			return 0;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		return list.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		String direct = list.get(position).get(Util.MEAL_CONTENT[2]);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			inflater = LayoutInflater.from(parent.getContext());
			if (direct.contains("right"))
				convertView = inflater.inflate(R.layout.item_order_right, null);
			else
				convertView = inflater.inflate(R.layout.item_order_left, null);
			viewHolder = new ViewHolder();
			viewHolder.text = (Button) convertView
					.findViewById(R.id.item_order_txt);
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.item_order_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text.setText(list.get(position).get(Util.MEAL_CONTENT[0]));
		String color = list.get(position).get(Util.MEAL_CONTENT[1]);
		if (color.contains("green"))
			viewHolder.img.setBackgroundResource(R.drawable.order_green);
		else if (color.contains("red")) {
			viewHolder.img.setBackgroundResource(R.drawable.order_red);
			if (position != mLastPosition) {
				mLastPosition = position;
			} else {
				mLastPosition = -1;
			}
			notifyDataSetChanged();
		}
		if (position == mLastPosition) {
			viewHolder.text.setTextColor(Color.parseColor("#f6242d"));
		} else {
			if (mLastPosition != -1) {
				viewHolder.text.setTextColor(Color.parseColor("#929292"));
			}
		}

		return convertView;
	}

	static class ViewHolder {
		public Button text;
		public ImageView img;
	}
}
