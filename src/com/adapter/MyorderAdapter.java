package com.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fragment.OrderInfoFragment;
import com.lease.R;
import com.util.Util;

public class MyorderAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	FragmentTransaction ft;
	Activity activity;
	private List<Map<String, String>> list;

	public MyorderAdapter(List<Map<String, String>> list, Activity activity,
			FragmentTransaction ft) {
		this.ft = ft;
		this.list = list;
		this.activity = activity;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.item_myorder_list, null);
			viewHolder = new ViewHolder();
			viewHolder.alllay = (LinearLayout) convertView
					.findViewById(R.id.myorder_lay);
			viewHolder.rtimg = (ImageView) convertView
					.findViewById(R.id.myorder_list_rigt);
			viewHolder.Nober = (TextView) convertView
					.findViewById(R.id.myorder_No);
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.myorder_list_txt);
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.myorder_list_img);
			viewHolder.cut = (View) convertView.findViewById(R.id.myorder_cut);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String content = list.get(position).get(Util.HOME_ITEM[0]);
		String color = list.get(position).get(Util.HOME_ITEM[1]);
		String cut = list.get(position).get(Util.HOME_ITEM[2]);
		String titl = list.get(position).get(Util.HOME_ITEM[3]);
		viewHolder.text.setText(content);
		viewHolder.Nober.setText(titl);
		if (color.equals("")) {
			viewHolder.alllay.setEnabled(false);
			viewHolder.img.setVisibility(View.INVISIBLE);
			viewHolder.rtimg.setVisibility(View.INVISIBLE);
		}
		if (cut.equals("1"))
			viewHolder.cut.setVisibility(View.VISIBLE);
		else
			viewHolder.cut.setVisibility(View.GONE);

		final int mposition = position;
		viewHolder.alllay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.id = Util.idlist[mposition];
				jpage(new OrderInfoFragment());
			}
		});

		return convertView;
	}

	void jpage(Fragment fragment) {
		Util.showProssbar(activity);
		ft.replace(R.id.main_content_ly, fragment);
		ft.commit();
	}

	static class ViewHolder {
		public LinearLayout alllay;
		public TextView text;
		public ImageView img;
		public View cut;
		public TextView Nober;
		public ImageView rtimg;
	}
}