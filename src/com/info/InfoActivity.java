package com.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;
import cn.jpush.android.api.JPushInterface;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adapter.AskForAdapter;
import com.base.BaseFragment;
import com.dto.GetVersonDto;
import com.dto.OrderDeviceDto;
import com.fragment.AskFor2Fragment;
import com.fragment.AskForTK;
import com.fragment.DeviceChooseFragment;
import com.fragment.IndexFragment;
import com.fragment.MindFragment;
import com.lease.R;
import com.main.MainActivity;
import com.net.NetWork;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.ChooseAreaView;
import com.tools.DivDialog;
import com.tools.ListViewHeightBasedOnChildren;
import com.tools.UpgradeUtil;
import com.util.ApkUtil;
import com.util.MyApplication;
import com.util.Util;


public class InfoActivity  extends BaseFragment implements OnClickListener {
	View view;
	WebView wvInfo;
	LayoutInflater inflater;
	Context context;
	static Message msg;
	LinearLayout llLoadFailed;
	ImageView ivLoadFailed;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		context = this.getActivity();
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.info_activity, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		init();

		return view;
	}
	
    private void init(){
    	wvInfo = (WebView) view.findViewById(R.id.wvBannerDetails);
    	llLoadFailed = (LinearLayout) view.findViewById(R.id.llLoadFailed);
    	ivLoadFailed = (ImageView) view.findViewById(R.id.ivLoadFailed);
    	WebSettings settings = wvInfo.getSettings();
    	settings.setAppCacheEnabled(true);
    	settings.setDatabaseEnabled(true);
    	settings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
    	settings.setCacheMode(WebSettings.LOAD_DEFAULT);    	
    	//WebView加载web资源
    	wvInfo.loadUrl(Util.infoUrl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开  	
    	wvInfo.setWebViewClient(new WebViewClient(){
           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	            view.loadUrl(url);
	            return true;
           }
           
           @Override
           public void onPageFinished(WebView view, String url){
                   //开始
                   super.onPageFinished(view, url);
                   creatMsg(0, 0);
           }
           @Override
           public void onPageStarted(WebView view, String url, Bitmap favicon){
                   //结束
                   super.onPageStarted(view, url, favicon);
           }
           @Override
           public void onReceivedError(WebView view, int errorCode,
                           String description, String failingUrl) {
                   super.onReceivedError(view, errorCode, description, failingUrl);
                   //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
                   //view.loadUrl(file:///android_asset/error.html );
                   //Util.showMsg(getActivity(), "Error Code:" + errorCode);
                   view.setVisibility(View.GONE);
                   llLoadFailed.setVisibility(View.VISIBLE); 
                   creatMsg(0, 0);
           }            
       });
    	
    	view.findViewById(R.id.back_btn).setOnClickListener(this);
    	view.findViewById(R.id.share_btn).setOnClickListener(this);
    }	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			Jpage(new IndexFragment());
			break;
		case R.id.share_btn:
			shareUtil("信息分享", "msgTitle", "msgText", "imgPath");
			break;			
		default:
			break;
		}
	}

	static void creatMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}



	static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Util.closeProssbar();
				break;
			case 1:
				break;
			default:
				break;
			}
		};
	};

    /**  
     * 分享功能  
     * @param context     上下文  
     * @param activityTitle  Activity的名字  
    * @param msgTitle  消息标题  
     * @param msgText  消息内容  
     * @param imgPath  图片路径，不分享图片则传null  
     */   
    public void shareUtil(String activityTitle, String msgTitle, String msgText,   
            String imgPath) {   
        Intent intent = new Intent(Intent.ACTION_SEND);   
        if (imgPath == null || imgPath.equals("")) {   
            intent.setType("text/plain"); // 纯文本   
        } else {   
            File f = new File(imgPath);   
            if (f != null && f.exists() && f.isFile()) {   
                intent.setType("image/jpg");   
              Uri u = Uri.fromFile(f);   
                intent.putExtra(Intent.EXTRA_STREAM, u);   
            }   
        }   
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);   
        intent.putExtra(Intent.EXTRA_TEXT, msgText);   
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        startActivity(Intent.createChooser(intent, activityTitle));   
    }
}