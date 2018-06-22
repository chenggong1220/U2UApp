package com.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fragment.DeviceChooseFragment;
import com.fragment.DeviceIntroduceFragment;
import com.fragment.MealFragment;
import com.lease.R;
import com.util.Util;

public class LegendAdapter extends BaseAdapter {
	List<Map<String, Object>> datalist;
	FragmentTransaction ft;
	Activity activity;
	DeviceChooseFragment dcFt;
	Map<Integer, List<String>> passmap = new HashMap<Integer, List<String>>();
	static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
	List<String> list = new ArrayList<String>();

	class ViewHolder {
		LinearLayout chooselay;
		ImageView meal;
		TextView txt;
		TextView name;
		TextView controlSystem;
		TextView mainShaftSpeed;
		TextView machinePower;
		CheckBox cb;
		ImageView view;
		View info;
	}

	public LegendAdapter(List<Map<String, Object>> datalist, Activity activity,
			FragmentTransaction ft) {
		this.ft = ft;
		this.datalist = datalist;
		this.activity = activity;
		initDate();
	}

	// 初始化isSelected的数据
	private void initDate() {
		// if (!Util.IsMealPage) {
		for (int i = 0; i < datalist.size(); i++) {
			getIsSelected().put(i, false);
		}
		// } else
		// Util.IsMealPage = false;
	}

	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public Object getItem(int position) {
		return datalist.get(position).get(Util.HOME_ITEM[0]);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Map<String, Object> datamap = datalist.get(position);
		final String bean = (String) datamap.get(Util.HOME_ITEM[0]);
		String control = (String) datamap.get(Util.HOME_ITEM[4]);
		String mainshaft = (String) datamap.get(Util.HOME_ITEM[5]);
		String power = (String) datamap.get(Util.HOME_ITEM[6]);
		Bitmap bitmap = (Bitmap) datamap.get(Util.HOME_ITEM[7]);
		String picolor = (String) datamap.get(Util.HOME_ITEM[2]);
		final String deposit = (String) datamap.get(Util.HOME_ITEM[3]);
		final String insurance = (String) datamap.get(Util.HOME_ITEM[8]);	//增加保险费，SUNZHE, 2017-03-26
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_device_choose_list,
					null);
			holder = new ViewHolder();
			holder.info = convertView.findViewById(R.id.choose_info);
			holder.view = (ImageView) convertView
					.findViewById(R.id.choose_view);
			holder.chooselay = (LinearLayout) convertView
					.findViewById(R.id.choose_lay);
			holder.meal = (ImageView) convertView
					.findViewById(R.id.choose_meal);
			holder.name = (TextView) convertView.findViewById(R.id.choose_name);
			holder.controlSystem = (TextView) convertView
					.findViewById(R.id.choose_controlSystem);
			holder.mainShaftSpeed = (TextView) convertView
					.findViewById(R.id.choose_mainShaftSpeed);
			holder.machinePower = (TextView) convertView
					.findViewById(R.id.choose_machinePower);
			holder.cb = (CheckBox) convertView.findViewById(R.id.choose_box);
			holder.txt = (TextView) convertView.findViewById(R.id.choose_txt);
			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(bean);
		holder.controlSystem.setText(control);
		holder.mainShaftSpeed.setText(mainshaft);
		holder.machinePower.setText(power);
		holder.txt.setText((String) datamap.get(Util.HOME_ITEM[1]));
		if (bitmap != null)
			holder.view.setImageBitmap(bitmap);
		else
			holder.view.setBackgroundResource(R.drawable.icon_error);
		final TextView txtview = holder.txt;
		if (picolor.equals("green")) {
			txtview.setTextColor(Color.parseColor("#1ea439"));
			holder.meal.setBackgroundResource(R.drawable.choose_money_green);
		} else {
			txtview.setTextColor(Color.parseColor("#f6242d"));
			holder.meal.setBackgroundResource(R.drawable.choose_money_red);
		}
		holder.info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.id = Util.idlist[position];
				jpage(new DeviceIntroduceFragment());
			}
		});

		holder.view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.id = Util.idlist[position];
				jpage(new DeviceIntroduceFragment());
			}
		});

		// 监听checkBox并根据原来的状态来设置新的状态
		holder.cb.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (isSelected.get(position)) {
					passmap.remove(position);
					dcFt.changeTxt(passmap);
					isSelected.put(position, false);
					setIsSelected(isSelected);
				} else {
					list = new ArrayList<String>();
					list.add(bean);
					list.add(txtview.getText().toString());
					list.add(deposit);
					list.add(Util.idlist[position]);
					list.add(Util.meal_id);
					list.add(insurance);	//增加保险费，SUNZHE, 2017-03-26
					passmap.put(position, list);
					dcFt.changeTxt(passmap);
					isSelected.put(position, true);
					setIsSelected(isSelected);
				}

			}
		});

		holder.chooselay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.chooseTag = position;
				Util.id = Util.idlist[position];
				jpage(new MealFragment());
			}
		});

		// 根据isSelected来设置checkbox的选中状况
		holder.cb.setChecked(getIsSelected().get(position));
		return convertView;
	}

	void jpage(Fragment fragment) {
		Util.showProssbar(activity);
		ft.replace(R.id.main_content_ly, fragment);
		ft.commit();
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		isSelected = isSelected;
	}
}