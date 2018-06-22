package com.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;

import com.util.Util;

public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener {
	private FragmentTransaction ft;

	public void Jpage(FragmentManager fm, Fragment tf, int view, Context context) {
		Util.showProssbar(context);
		ft = fm.beginTransaction();
		ft.replace(view, tf);
		ft.commit();
	}

}
