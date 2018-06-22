package com.tools;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lease.R;
import com.util.Util;

public class CreatView {
	static TextView txt, type, rule, rate, attention, time;

	public static View creat(LayoutInflater inflater, JSONObject mark, int i)
			throws JSONException {
		Util.mealid[i] = (String) mark.getString("id");
		Util.mealname[i] = (String) mark.getString("mealName");
		View view = inflater.inflate(R.layout.item_meal_case, null);
		txt = (TextView) view.findViewById(R.id.case_text);
		type = (TextView) view.findViewById(R.id.case_type);
		rule = (TextView) view.findViewById(R.id.case_rule);
		rate = (TextView) view.findViewById(R.id.case_rate);
		attention = (TextView) view.findViewById(R.id.case_attention);
		time = (TextView) view.findViewById(R.id.case_time);
		txt.setText(Util.mealname[i]);
		String mealtype = (String) mark.getString("mealType");
		type.setText(mealtype.equals("0") ? "分时租赁" : "分月租赁");
		rule.setText((String) mark.getString("accountRules"));
		String ratecontent = (String) mark.getString("rate");
		rate.setText(ratecontent);
		attention.setText((String) mark.getString("attentionItem"));
		time.setText((String) mark.getString("miniUserdTime"));
		return view;
	}
}
