package com.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager.ImageCycleViewListener;

import com.base.BaseFragment;
import com.dto.ADInfo;
import com.info.InfoActivity;
import com.lease.R;
import com.login.LoginActivity;
import com.main.MainActivity;
import com.net.Config;
import com.net.NetWork;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.slidingmenu.lib.SlidingMenu;
import com.tools.BitmapHelper;
import com.tools.ViewFactory;
import com.util.MyApplication;
import com.util.Util;

public class IndexFragment extends BaseFragment implements OnItemClickListener {
	View view;
	Message msg;
	static ListView listview;
	static String[] idlist;
	static String[] imapath;
	List<ImageView> views = new ArrayList<ImageView>();
	List<ADInfo> infos = new ArrayList<ADInfo>();
	CycleViewPager cycleViewPager = new CycleViewPager();
	String NESTED_FRAGMENT_TAG = "0";
	FragmentTransaction fragmentTransaction;
	static SimpleAdapter adapter;

	// 临时网络图片
	private String[] imageUrls = {
			Config.ImgUrl + "styles/mobile/banner-01.png",
			Config.ImgUrl + "styles/mobile/banner-02.png",
			Config.ImgUrl + "styles/mobile/banner-03.png",
			Config.ImgUrl + "styles/mobile/banner-04.png",
			Config.ImgUrl + "styles/mobile/banner-05.png" };
	
	private String[] contentUrls = {
			"http://www.baidu.com",
			"http://www.jihui.tech",
			"http://www.sina.com.cn",
			"http://www.163.com",
			"http://www.unislease.com", };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.fragmentTag = this.toString().split("[{]")[0];
		view = inflater.inflate(R.layout.index_fragment, null);
		Util.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		init();
		sendMsg(0, 100);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_menu:
			Util.slidingMenu.toggle();
			break;

		case R.id.index_time_leasing:
			if (!MainActivity.chanFoot(-1)) {
				Util.indexType = "分时租赁";
				Jpage(new DeviceChooseFragment());
			}
			break;

		case R.id.index_month_leasing:
			if (!MainActivity.chanFoot(-1)) {
				Util.indexType = "分月租赁";
				Jpage(new DeviceChooseFragment());
			}
			break;

		case R.id.index_more:
			if (!MainActivity.chanFoot(-1)) {
				Util.indexType = "分时租赁";
				Jpage(new DeviceChooseFragment());
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.home_lv:
			if (!MainActivity.chanFoot(-1)) {
				Util.id = idlist[position];
				Jpage(new DeviceIntroduceFragment());
			}
			break;

		default:
			break;
		}
	}

	// 临时数据
	public static List<Map<String, Object>> getData() {
		Object obj = null;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		Object result = NetWork.NetResult("device/getStarAsset", null,
				"getStarAsset");
		if (result == null) {
			Util.showMsg(MyApplication.getAppContext(), "服务器或网络异常！");
		} else {
			JSONArray list = Util.json2ary(result.toString());
			idlist = new String[list.length()];
			for (int i = 0; i < idlist.length; i++) {
				map = new HashMap<String, Object>();
				try {
					JSONObject mark = (JSONObject) list.get(i);
					idlist[i] = (String) mark.getString("id");
					map.put(Util.HOME_ITEM[0], (String) mark.getString("model"));
					if (((String) mark.getString("type")).equals("0"))
						map.put(Util.HOME_ITEM[1], "分时租赁");
					else
						map.put(Util.HOME_ITEM[1], "分月租赁");
					obj = Util.btmap.get(idlist[i]);
					if (obj != null)
						map.put(Util.HOME_ITEM[2], (Bitmap) obj);
					else {
						try {
							obj = new BitmapHelper(Config.ImgUrl
									+ (String) mark.getString("imgPath"))
									.execute().get();
							map.put(Util.HOME_ITEM[2], obj);
							Util.btmap.put(idlist[i], (Bitmap) obj);
						} catch (InterruptedException e) {
						} catch (ExecutionException e) {
						}
					}
					map.put(Util.HOME_ITEM[3],
							(String) mark.getString("deposit"));
					map.put(Util.HOME_ITEM[4],
							(String) mark.getString("controlSystem"));
					map.put(Util.HOME_ITEM[5],
							(String) mark.getString("mainShaftSpeed"));
					map.put(Util.HOME_ITEM[6],
							(String) mark.getString("machinePower"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				data.add(map);
			}
		}
		return data;
	}

	// 初始化数值
	void init() {
		MainActivity.chanFoot(0);
		fragmentTransaction = getChildFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.home_advert, cycleViewPager,
				NESTED_FRAGMENT_TAG).commit();
		listview = (ListView) view.findViewById(R.id.home_lv);
		listview.setOnItemClickListener(this);
		view.findViewById(R.id.home_menu).setOnClickListener(this);
		view.findViewById(R.id.index_more).setOnClickListener(this);
		view.findViewById(R.id.index_time_leasing).setOnClickListener(this);
		view.findViewById(R.id.index_month_leasing).setOnClickListener(this);
	}

	void sendMsg(int what, long time) {
		msg = new Message();
		msg.what = what;
		handler.sendMessageDelayed(msg, time);
	}

	public static void refresh() {
		adapter = new SimpleAdapter(MyApplication.getAppContext(), getData(),
				R.layout.item_home_list, Util.HOME_ITEM, new int[] {
						R.id.item_homedevice_name, R.id.item_homedevice_type,
						R.id.item_homedevice_img, R.id.item_homedevice_deposit,
						R.id.item_homedevice_controlSystem,
						R.id.item_homedevice_mainShaftSpeed,
						R.id.item_homedevice_machinePower });
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap) data);
					return true;
				} else
					return false;
			}
		});
		listview.setAdapter(adapter);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				configImageLoader();
				creatCycleView();
				refresh();
				Util.closeProssbar();
				break;

			default:
				break;
			}
		};
	};

	void creatCycleView() {
		for (int i = 0; i < imageUrls.length; i++) {
			ADInfo info = new ADInfo();
			info.url = imageUrls[i];
			info.content = contentUrls[i];
			infos.add(info);
		}
		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(this.getActivity(),
				infos.get(infos.size() - 1).url));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(this.getActivity(),
					infos.get(i).url));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(this.getActivity(), infos.get(0).url));
		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);
		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, infos, mAdCycleViewListener);
		// 设置轮播
		cycleViewPager.setWheel(true);
		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(2000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
			}

//			Util.infoUrl = contentUrls[position];  //Banner点开功能，暂时屏蔽
//			Jpage(new InfoActivity());				//Banner点开功能，暂时屏蔽
			//Util.infoUrl = "";
		}
	};

	private void configImageLoader() {
		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_empty) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(false) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this.getActivity()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}
}