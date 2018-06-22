package com.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.lease.R;

public class DivDialog {
	Dialog dialog;
	Context context;
	LayoutInflater inflater;

	public DivDialog(Context context) {
		this.context = context;
	}

	public void show() {
		inflater = LayoutInflater.from(context);

		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_wait, null);

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);
	}

	public void dismiss() {
		if (dialog == null)
			return;
		dialog.dismiss();
		dialog = null;
		inflater = null;
	}
}
