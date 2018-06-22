package com.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dto.CItem;

public class SpinnerAdapter extends ArrayAdapter<CItem> {
	Context context;
	List<CItem> items;

	public SpinnerAdapter(final Context context, final int textViewResourceId,
			final List<CItem> objects) {
		super(context, textViewResourceId, objects);
		this.items = objects;
		this.context = context;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_item, parent, false);
		}

		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(items.get(position).Value);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(15);
		tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		return convertView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_item, parent, false);
		}

		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(items.get(position).Value);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(15);
		tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		return convertView;
	}
}