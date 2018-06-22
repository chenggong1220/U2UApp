package com.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;

import com.lease.R;
import com.main.MainActivity;
import com.util.Util;

public abstract class BaseFragment extends Fragment implements OnClickListener {
	public void Jpage(Fragment fragment) {
		MainActivity activity = (MainActivity) getActivity();
		Util.showProssbar(activity);
		FragmentManager fm = activity.getSupportFragmentManager();
		if (Util.bundle.containsKey("meal"))
			fragment.setArguments(Util.bundle);
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.main_content_ly, fragment);
		ft.commit();
	}
}
