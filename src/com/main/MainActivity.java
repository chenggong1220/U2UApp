package com.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.base.BaseActivity;
import com.fragment.AboutFragment;
import com.fragment.CustomerFragment;
import com.fragment.FeedbackFragment;
import com.fragment.HelpFragment;
import com.fragment.IndexFragment;
import com.fragment.MineFragment;
import com.fragment.NoteFragment;
import com.fragment.OrderFragment;
import com.fragment.WholeSettingFragment;
import com.lease.R;
import com.login.LoginActivity;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.SaveUserInfo;
import com.util.ApkUtil;
import com.util.ExitApplication;
import com.util.Util;

public class MainActivity extends BaseActivity {
	FragmentManager fm;
	boolean isExit;
	View view;
	PopupWindow window;
	Message msg;
	static int[] draw = { R.drawable.foot_home_sel, R.drawable.foot_order_sel,
			R.drawable.foot_note_sel, R.drawable.foot_mine_sel };
	static int[] draw_hide = { R.drawable.foot_home_nosel,
			R.drawable.foot_order_nosel, R.drawable.foot_note_nosel,
			R.drawable.foot_mine_nosel };
	static ImageView homeImg, orderImg, noteImg, mineImg, footred;
	static ImageView[] imgList = { homeImg, orderImg, noteImg, mineImg };
	static TextView homeTxt, orderTxt, noteTxt, mineTxt;
	static TextView[] txtList = { homeTxt, orderTxt, noteTxt, mineTxt };
	static int[] imglist = { R.id.home_img, R.id.order_img, R.id.note_img,
			R.id.mine_img };
	static int[] txtlist = { R.id.home_txt, R.id.order_txt, R.id.note_txt,
			R.id.mine_txt };
	static TextView sysUser, sysInfo;
	static LinearLayout exitlay;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_activity);
		fm = getSupportFragmentManager();
		if (Util.slidingMenu == null)
			creatSliding();
		if (Util.mainTag) {
			Util.mainTag = false;
			creatSliding();
		}

		for (int i = 0; i < imglist.length; i++) {
			imgList[i] = (ImageView) findViewById(imglist[i]);
			txtList[i] = (TextView) findViewById(txtlist[i]);
		}
		footred = (ImageView) findViewById(R.id.foot_notice_red);
		view = findViewById(R.id.footer_home);
		view.setOnClickListener(this);
		findViewById(R.id.footer_order).setOnClickListener(this);
		findViewById(R.id.footer_note).setOnClickListener(this);
		findViewById(R.id.footer_mine).setOnClickListener(this);
		view.performClick();

		ExitApplication.getInstance().addActivity(this);
	}

	@Override
	public void onBackPressed() {
		if (Util.slidingMenu.isMenuShowing())
			Util.slidingMenu.toggle();
		else {
			if (!isExit) {
				isExit = true;
				Toast.makeText(this,
						getResources().getString(R.string.plc_toast_msg_be),
						Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					public void run() {
						isExit = false;
					}
				}, 2000);
				return;
			}
			ExitApplication.getInstance().exit();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.footer_home:
			Util.bundle.clear();
			Util.slidingTag = -1;
			if (!Util.fragmentTag.equals("IndexFragment"))
				Jpage(fm, new IndexFragment(), R.id.main_content_ly, this);
			break;

		case R.id.footer_order:
			Util.bundle.clear();
			if (chanFoot(1))
				return;
			Util.slidingTag = -1;
			if (!Util.fragmentTag.equals("OrderFragment"))
				Jpage(fm, new OrderFragment(), R.id.main_content_ly, this);
			break;

		case R.id.footer_note:
			Util.bundle.clear();
			if (chanFoot(2))
				return;
			Util.slidingTag = -1;
			if (!Util.fragmentTag.equals("NoteFragment"))
				Jpage(fm, new NoteFragment(), R.id.main_content_ly, this);
			break;

		case R.id.footer_mine:
			Util.bundle.clear();
			if (chanFoot(3))
				return;
			Util.slidingTag = -1;
			if (!Util.fragmentTag.equals("MineFragment"))
				Jpage(fm, new MineFragment(), R.id.main_content_ly, this);
			break;

		case R.id.sys_out:
			Util.slidingMenu.toggle();
			break;

		case R.id.sys_img:
			if (sysInfo.getText().toString().equals("登录")) {
				Util.slidingMenu.toggle();
				sendMsg(1, 200);
			}
			break;

		case R.id.home_menu:
			Util.slidingMenu.toggle();
			break;

		case R.id.sys_exit:
			Util.slidingMenu.toggle();
			SaveUserInfo.clear();
			exitlay.setVisibility(View.INVISIBLE);
			sysInfo.setText("登录");
			sysUser.setVisibility(View.GONE);
			sendMsg(6, 200);
			break;

		case R.id.sys_info:
			if (sysInfo.getText().toString().equals("登录")) {
				Util.slidingMenu.toggle();
				sendMsg(1, 200);
			}
			break;
		case R.id.customer_lay:
			Util.slidingMenu.toggle();
			if (Util.slidingTag != 1)
				sendMsg(2, 200);
			break;

		case R.id.setting_lay:
			Util.slidingMenu.toggle();
			if (Util.slidingTag != 4)
				sendMsg(3, 200);
			break;

		case R.id.feedback_lay:
			Util.slidingMenu.toggle();
			if (Util.slidingTag != 2)
				sendMsg(4, 200);
			break;

		case R.id.about_lay:
			Util.slidingMenu.toggle();
			if (Util.slidingTag != 3)
				sendMsg(5, 200);
			break;
		case R.id.help_lay:
			Util.slidingMenu.toggle();
			if (Util.slidingTag != 1)
				sendMsg(7, 200);
			break;
		default:
			break;
		}
	}

	public static boolean chanFoot(int tag) {
		boolean b = !Util.checklogin();
		if (b || tag == 0) {
			if (tag == -1) {
				for (int i = 0; i < imglist.length; i++) {
					imgList[i].setBackgroundResource(draw_hide[i]);
					txtList[i].setTextColor(Color.parseColor("#929292"));
				}
			} else {
				for (int i = 0; i < imglist.length; i++) {
					if (i == tag) {
						imgList[i].setBackgroundResource(draw[i]);
						txtList[i].setTextColor(Color.parseColor("#1ea439"));
					} else {
						imgList[i].setBackgroundResource(draw_hide[i]);
						txtList[i].setTextColor(Color.parseColor("#929292"));
					}
				}
			}
		}
		return !b;
	}

	public static void setRed() {
		if (footred != null) {
			Util.fragmentTag = "";
			footred.setVisibility(View.VISIBLE);
		}
	}

	public static void hideRed() {
		if (footred != null && footred.getVisibility() == View.VISIBLE)
			footred.setVisibility(View.INVISIBLE);
	}

	public static void chanUser() {
		Util.userlist = SaveUserInfo.relist();

		if (Util.userlist.size() != 0) {
			sysUser.setText(Util.userlist.get(0).substring(1));
			sysInfo.setText("当前用户 : ");
			sysUser.setVisibility(View.VISIBLE);
			exitlay.setVisibility(View.VISIBLE);
		}
	}

	void creatSliding() {
		Util.slidingMenu = new SlidingMenu(this);
		Util.slidingMenu.setMode(SlidingMenu.LEFT);
		Util.slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		Util.slidingMenu.setMenu(R.layout.slidingmenu);
		Util.slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sysUser = (TextView) Util.slidingMenu.findViewById(R.id.sys_user);
		sysInfo = (TextView) Util.slidingMenu.findViewById(R.id.sys_info);
		exitlay = (LinearLayout) Util.slidingMenu.findViewById(R.id.sys_exit);
		exitlay.setOnClickListener(this);
		sysInfo.setOnClickListener(this);
		Util.slidingMenu.findViewById(R.id.sys_out).setOnClickListener(this);
		Util.slidingMenu.findViewById(R.id.sys_img).setOnClickListener(this);
		Util.slidingMenu.findViewById(R.id.help_lay).setOnClickListener(this);
		Util.slidingMenu.findViewById(R.id.customer_lay).setOnClickListener(this);
		Util.slidingMenu.findViewById(R.id.feedback_lay).setOnClickListener(this);
		Util.slidingMenu.findViewById(R.id.about_lay).setOnClickListener(this);
		Util.slidingMenu.findViewById(R.id.setting_lay).setOnClickListener(this);
		
		//Set Current App Version, SUNZHE, 2017-03-24
		TextView curVersion = (TextView)Util.slidingMenu.findViewById(R.id.tvVersion);
		curVersion.setText("当前版本：" + ApkUtil.getVersionName(this));
		checkUser();
	}

	void sendMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	void checkUser() {
		SaveUserInfo.reuser();
		Util.userlist = SaveUserInfo.relist();
		if (Util.userlist.size() != 0) {
			sysInfo.setText("当前用户 : ");
			sysUser.setText(Util.userlist.get(0).substring(1));
			sysUser.setVisibility(View.VISIBLE);
			exitlay.setVisibility(View.VISIBLE);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				break;

			case 2:
				if (chanFoot(-1))
					return;
				Jpage(fm, new CustomerFragment(), R.id.main_content_ly,
						MainActivity.this);
				break;

			case 3:
				if (chanFoot(-1))
					return;
				Jpage(fm, new WholeSettingFragment(), R.id.main_content_ly,
						MainActivity.this);
				break;

			case 4:
				if (chanFoot(-1))
					return;
				Jpage(fm, new FeedbackFragment(), R.id.main_content_ly,
						MainActivity.this);
				break;

			case 5:
				if (chanFoot(-1))
					return;
				Jpage(fm, new AboutFragment(), R.id.main_content_ly,
						MainActivity.this);
				break;

			case 6:
				view.performClick();
				break;
			case 7:
				if (chanFoot(-1))
					return;
				Jpage(fm, new HelpFragment(), R.id.main_content_ly,
						MainActivity.this);
				break;
			default:
				break;
			}
		};
	};

}
