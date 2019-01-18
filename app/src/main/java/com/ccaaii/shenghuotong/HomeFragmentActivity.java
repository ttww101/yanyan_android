package com.ccaaii.shenghuotong;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccaaii.base.utils.StatusBarUtils;
import com.ccaaii.shenghuotong.api.ApiUtils;
import com.ccaaii.shenghuotong.api.IResponseListener;
import com.ccaaii.shenghuotong.bean.InitBean;
import com.ccaaii.shenghuotong.bean.LocationBean;
import com.ccaaii.shenghuotong.fragment.PageOneFragment;
import com.ccaaii.shenghuotong.setting.MeFragment;
import com.google.gson.reflect.TypeToken;
import com.ccaaii.base.utils.CharUtils;
import com.ccaaii.shenghuotong.bean.CityInfo;
import com.ccaaii.shenghuotong.city.CityDAO;
import com.ccaaii.shenghuotong.discover.DiscoverFragment;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.Compatibility;
import com.ccaaii.base.utils.FileUtils;
import com.ccaaii.base.utils.ImageLoader;
import com.ccaaii.base.utils.widgets.FixedSpeedScroller;
import com.ccaaii.base.utils.widgets.HomeBottomButton;
import com.ccaaii.shenghuotong.home.HomeFragment;
import com.ccaaii.shenghuotong.setting.SettingFragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class HomeFragmentActivity extends FragmentActivity {

	private static final String TAG = "[CCAAII]HomeFragmentActivity";

	private static final int SCREEN_HOME = 0;
	private static final int SCREEN_DISCOVERY = 1;
	private static final int SCREEN_SETTING = 2;
	private static final int SCREEN_ME = 3;
	private ProgressDialog pd;
	private HomeFragmentPagerAdapter mHomeFragmentPagerAdapter;
	private ViewPager mViewPager;

	private ArrayList<Fragment> mFragmentsList;

	private HomeFragment mHomeFragment;
	private DiscoverFragment mDiscoverFragment;
	private SettingFragment mSettingFragment;
	private MeFragment meFragment;

	private HomeBottomButton mButtonHome;
	private HomeBottomButton mButtonDiscovery;
	private HomeBottomButton mButtonSetting;
	private HomeBottomButton mButtonMe;

	private TextView mTitleView;
	private WebView wb;
	private View empty_view;
	private RelativeLayout top_layout;
	private int height;
	private int mCurrentScreen = SCREEN_HOME;
	
	private Bundle mSavedInstanceState;

	private ImageLoader mImageLoader;
	private List<String> mKeyList;

	public ImageLoader getImageLoader(){
		if (mImageLoader == null){
			mImageLoader = new ImageLoader(this, ImageLoader.TYPE_SHOW_BIG_IMAGE);
		}
		return mImageLoader;
	}

	public void addKeyString(String key){
		if (mKeyList == null){
			mKeyList = new ArrayList<String>();
		}
		if (!TextUtils.isEmpty(key)){
			mKeyList.add(key);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mSavedInstanceState = savedInstanceState;

		setContentView(R.layout.h_layout);
		StatusBarUtils.setColor(this, this.getResources().getColor(R.color.style_color));

		WindowManager wm = this.getWindowManager();
		height = wm.getDefaultDisplay().getHeight();

		buildLayout();
		initViewPager();
		init();

		mImageLoader = new ImageLoader(this, ImageLoader.TYPE_SHOW_BIG_IMAGE);
		mKeyList = new ArrayList<String>();

//		DocumentUtils.testParseData(this);

		try {
			File dirFile = new File(FileUtils.LOCAL_FILE_PATH);
			if (!dirFile.exists()){
				dirFile.mkdirs();
			}
			FileUtils.copyAsserts(this, "share", FileUtils.LOCAL_FILE_PATH);
		} catch (Exception ex){

		}

		initProvinceCityDB();

		syncLocation();

	}

	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	public void onPause(){
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		if (mHomeFragmentPagerAdapter != null){
			mHomeFragmentPagerAdapter = null;
		}

		if (mImageLoader != null){
			mImageLoader.clearCache(mKeyList);
			mImageLoader = null;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		try {
			this.getSupportFragmentManager().putFragment(outState, HomeFragment.TAG, mHomeFragment);
			this.getSupportFragmentManager().putFragment(outState, DiscoverFragment.TAG, mDiscoverFragment);
			this.getSupportFragmentManager().putFragment(outState, SettingFragment.TAG, mSettingFragment);
			this.getSupportFragmentManager().putFragment(outState, MeFragment.TAG, meFragment);

		} catch (Exception ex){
		}

	}

	private long mBackTime = 0;
	@Override
	public void onBackPressed() {
		if (mBackTime == 0){
			mBackTime = System.currentTimeMillis();
			showToast(R.string.toast_back_again_to_finish, Toast.LENGTH_SHORT);
		} else {
			if (System.currentTimeMillis() - mBackTime <= 1000){
				super.onBackPressed();
			} else {
				showToast(R.string.toast_back_again_to_finish, Toast.LENGTH_SHORT);
				mBackTime = System.currentTimeMillis();
			}
		}
	}

	private void buildLayout() {
		if (LogLevel.DEV) {
			DevLog.d(TAG, "buildLayout...");
		}

		mTitleView = (TextView) this.findViewById(R.id.home_title_view);
		mTitleView.setText(R.string.bottom_home);


		if(mSavedInstanceState != null){
			try {
				mHomeFragment = (HomeFragment) this.getSupportFragmentManager().getFragment(mSavedInstanceState, HomeFragment.TAG);
				mDiscoverFragment = (DiscoverFragment) this.getSupportFragmentManager().getFragment(mSavedInstanceState, DiscoverFragment.TAG);
				mSettingFragment = (SettingFragment) this.getSupportFragmentManager().getFragment(mSavedInstanceState, SettingFragment.TAG);
				meFragment = (MeFragment) this.getSupportFragmentManager().getFragment(mSavedInstanceState, MeFragment.TAG);

			} catch (Exception ex){
				MarketLog.e(TAG, "initViewPager init fragment from saved instance state failed, ex : " + ex.getMessage());
			}

		}
		if(mHomeFragment == null){
			MarketLog.w(TAG, "initViewPager, home fragment is null.");
			mHomeFragment = HomeFragment.newInstance();
		}

		if(mDiscoverFragment == null){
			MarketLog.w(TAG, "initViewPager, discovery  fragment is null.");
			mDiscoverFragment = DiscoverFragment.newInstance();
		}

		if(mSettingFragment == null){
			MarketLog.w(TAG, "initViewPager, setting fragment is null.");
			mSettingFragment = SettingFragment.newInstance();
		}

		if(meFragment == null){
			MarketLog.w(TAG, "initViewPager, me fragment is null.");
			meFragment = MeFragment.newInstance();
		}

		mFragmentsList = new ArrayList<Fragment>();
		mFragmentsList.add(mHomeFragment);
		mFragmentsList.add(mDiscoverFragment);
		mFragmentsList.add(mSettingFragment);
		mFragmentsList.add(meFragment);

		mButtonHome = (HomeBottomButton) this.findViewById(R.id.home_bottom_button_home);
		mButtonDiscovery = (HomeBottomButton) this.findViewById(R.id.home_bottom_button_discovery);
		mButtonSetting = (HomeBottomButton) this.findViewById(R.id.home_bottom_button_setting);
		mButtonMe = (HomeBottomButton) this.findViewById(R.id.home_bottom_button_me);

		wb = (WebView) this.findViewById(R.id.wb);
		empty_view = (View) this.findViewById(R.id.empty_view);
		top_layout = (RelativeLayout) this.findViewById(R.id.top_layout);

		mButtonHome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurrentScreen == SCREEN_HOME) {
					return;
				}
				mViewPager.setCurrentItem(SCREEN_HOME, false);
				wb.setVisibility(View.VISIBLE);
				wb.loadUrl("https://kj.13322.com/pk10_statistics.html");
				wb.setWebViewClient(new WebViewClient()
				{
					public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
					{
						super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
						pd.dismiss();
						empty_view.setVisibility(View.VISIBLE);
                        top_layout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, (int)(height * 0.062)));

					}

					public void onPageStarted(WebView paramAnonymousWebView, String paramAnonymousString, Bitmap paramAnonymousBitmap)
					{
						super.onPageStarted(paramAnonymousWebView, paramAnonymousString, paramAnonymousBitmap);
						pd.show();
					}

					public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2)
					{
						super.onReceivedError(paramAnonymousWebView, paramAnonymousInt, paramAnonymousString1, paramAnonymousString2);
						wb.loadUrl("https://kj.13322.com/pk10_statistics.html");
						if (paramAnonymousInt < 0) {}
					}

					public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
					{
						paramAnonymousWebView.loadUrl(paramAnonymousString);
						return false;
					}
				});
			}
		});

		mButtonDiscovery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurrentScreen == SCREEN_DISCOVERY) {
					return;
				}
				mViewPager.setCurrentItem(SCREEN_DISCOVERY, false);
				wb.setVisibility(View.VISIBLE);
				wb.loadUrl("https://kj.13322.com/pk10_history_dtoday.html");
				wb.setWebViewClient(new WebViewClient()
				{
					public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
					{
						super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
						pd.dismiss();
                        top_layout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, (int)(height * 0.067)));
						empty_view.setVisibility(View.VISIBLE);
					}

					public void onPageStarted(WebView paramAnonymousWebView, String paramAnonymousString, Bitmap paramAnonymousBitmap)
					{
						super.onPageStarted(paramAnonymousWebView, paramAnonymousString, paramAnonymousBitmap);
						pd.show();
					}

					public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2)
					{
						super.onReceivedError(paramAnonymousWebView, paramAnonymousInt, paramAnonymousString1, paramAnonymousString2);
						wb.loadUrl("https://kj.13322.com/pk10_history_dtoday.html");
						if (paramAnonymousInt < 0) {}
					}

					public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
					{
						paramAnonymousWebView.loadUrl(paramAnonymousString);
						return false;
					}
				});
			}
		});

		mButtonSetting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurrentScreen == SCREEN_SETTING) {
					return;
				}
				mViewPager.setCurrentItem(SCREEN_SETTING, false);
				wb.setVisibility(View.VISIBLE);
				wb.loadUrl("https://kj.13322.com/pk10_BaseTrend_30.html");
				wb.setWebViewClient(new WebViewClient()
				{
					public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
					{
						super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
						pd.dismiss();
						empty_view.setVisibility(View.VISIBLE);
						top_layout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, (int)(height * 0.067)));
					}

					public void onPageStarted(WebView paramAnonymousWebView, String paramAnonymousString, Bitmap paramAnonymousBitmap)
					{
						super.onPageStarted(paramAnonymousWebView, paramAnonymousString, paramAnonymousBitmap);
						pd.show();
					}

					public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2)
					{
						super.onReceivedError(paramAnonymousWebView, paramAnonymousInt, paramAnonymousString1, paramAnonymousString2);
						wb.loadUrl("https://kj.13322.com/pk10_BaseTrend_30.html");
						if (paramAnonymousInt < 0) {}
					}

					public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
					{
						paramAnonymousWebView.loadUrl(paramAnonymousString);
						return false;
					}
				});
			}
		});

		mButtonMe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentScreen == SCREEN_ME) {
					return;
				}
				mViewPager.setCurrentItem(SCREEN_ME, false);
                wb.setVisibility(View.GONE);
				top_layout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, (int)(height * 0.04)));
			}
		});
		updateBottomButton();
	}

	private void updateBottomButton() {
		if (mCurrentScreen == SCREEN_HOME) {
			mTitleView.setText(R.string.bottom_home);
			mButtonHome.setSelected(true);
			mButtonDiscovery.setSelected(false);
			mButtonSetting.setSelected(false);
			mButtonMe.setSelected(false);
		} else if (mCurrentScreen == SCREEN_DISCOVERY) {
			mTitleView.setText(R.string.bottom_discovery);
			mButtonHome.setSelected(false);
			mButtonDiscovery.setSelected(true);
			mButtonSetting.setSelected(false);
			mButtonMe.setSelected(false);
		} else if (mCurrentScreen == SCREEN_SETTING){
			mTitleView.setText(R.string.bottom_setting);
			mButtonHome.setSelected(false);
			mButtonDiscovery.setSelected(false);
			mButtonSetting.setSelected(true);
			mButtonMe.setSelected(false);
		} else if (mCurrentScreen == SCREEN_ME){
			mTitleView.setText(R.string.bottom_me);
			mButtonHome.setSelected(false);
			mButtonDiscovery.setSelected(false);
			mButtonSetting.setSelected(false);
			mButtonMe.setSelected(true);
		}
	}

	private void initViewPager() {
		mHomeFragmentPagerAdapter = new HomeFragmentPagerAdapter(this.getSupportFragmentManager(), mFragmentsList);
		mViewPager = (ViewPager) findViewById(R.id.home_view_pager);
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), 1000);
			mScroller.set(mViewPager, scroller);
		} catch (Exception e) {
			if (LogLevel.MARKET) {
				MarketLog.e(TAG, "Set viewpager scoller failed. e = " + e.toString());
			}
		}
		mViewPager.setAdapter(mHomeFragmentPagerAdapter);
		mViewPager.setOffscreenPageLimit(5);
		mViewPager.setCurrentItem(SCREEN_HOME);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int position) {
				mCurrentScreen = position;
				updateBottomButton();

			}

		});
	}

	private Toast mToast;

	public void showToast(int message, int time) {
		showToast(this.getString(message), time);
	}

	public void showToast(String message, int time) {
		if (mToast == null) {
			mToast = Toast.makeText(this, message, time);
		} else {
			mToast.setText(message);
			mToast.setDuration(time);
		}
		if (Compatibility.getApiLevel() < 14) {
			mToast.cancel();
		}
		mToast.show();
	}

	private void syncLocation(){
		ApiUtils.getLocationByIp(CcaaiiApp.getCcaaiiContext(), new IResponseListener() {
			@Override
			public void onResponseResult(boolean success, int code, String msg) {
				if (success){
					LocationBean bean = CcaaiiApp.getGson().fromJson(msg, new TypeToken<LocationBean>(){}.getType());
					if (bean != null){
						CcaaiiApp.setCountry(bean.getCountry());
						CcaaiiApp.setProvince(bean.getProvince());
						CcaaiiApp.setCity(bean.getCity());

						mHomeFragment.refreshWeather(true);
					}
				}
			}
		});
	}

	private void initProvinceCityDB(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				MarketLog.w(TAG, "Start init city");
				if (CityDAO.hasSavedCity(CcaaiiApp.getCcaaiiContext())){
					MarketLog.w(TAG, "Has saved city.");
					return;
				}
				String cityInfoStr = CharUtils.getFromAssets(CcaaiiApp.getCcaaiiContext(), "cncity.json");
				CityInfo cityInfo = CcaaiiApp.getGson().fromJson(cityInfoStr, new TypeToken<CityInfo>() {}.getType());
				CityDAO.saveCity(CcaaiiApp.getCcaaiiContext(), cityInfo);
				MarketLog.w(TAG, "End init city");
			}
		});
		thread.start();
	}

	private void init()
	{
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, (int)(height * 0.15));
		layoutParams.addRule(RelativeLayout.ABOVE, R.id.bottom_layout);
		empty_view.setLayoutParams(layoutParams);
		empty_view.setVisibility(View.VISIBLE);
		top_layout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, (int)(height * 0.062)));
		this.pd = new ProgressDialog(this);
		this.pd.setMessage(getString(R.string.loading_points));
		WebSettings paramView = this.wb.getSettings();
		paramView.setUseWideViewPort(true);
		paramView.setLoadWithOverviewMode(true);
		paramView.setDisplayZoomControls(true);
		paramView.setDomStorageEnabled(true);
		paramView.setDatabaseEnabled(true);
		paramView.setJavaScriptEnabled(true);
		//paramView.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		paramView.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
		paramView.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
		paramView.setAppCacheEnabled(true);//是否使用缓存
		paramView.setRenderPriority(WebSettings.RenderPriority.HIGH);
		paramView.setCacheMode(WebSettings.LOAD_DEFAULT);
		paramView.setDomStorageEnabled(true);//DOM Storage
		String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";
		paramView.setDatabasePath(cacheDirPath);
		paramView.setAppCachePath(cacheDirPath);

		wb.setVisibility(View.VISIBLE);

		this.wb.setWebChromeClient(new WebChromeClient());
		if (Build.VERSION.SDK_INT > 21) {
			CookieManager.getInstance().setAcceptThirdPartyCookies(this.wb, true);
		}
		for (;;)
		{
			if ((Build.VERSION.SDK_INT >= 19) && ((getApplicationInfo().flags & 0x2) != 0))
			{
				WebView.setWebContentsDebuggingEnabled(true);
			}
			this.wb.setWebViewClient(new WebViewClient()
			{
				public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
				{
					super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
					pd.dismiss();
				}

				public void onPageStarted(WebView paramAnonymousWebView, String paramAnonymousString, Bitmap paramAnonymousBitmap)
				{
					super.onPageStarted(paramAnonymousWebView, paramAnonymousString, paramAnonymousBitmap);
					pd.show();
				}

				public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2)
				{
					super.onReceivedError(paramAnonymousWebView, paramAnonymousInt, paramAnonymousString1, paramAnonymousString2);
					wb.loadUrl("https://kj.13322.com/pk10_statistics.html");
					if (paramAnonymousInt < 0) {}
				}

				public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
				{
					paramAnonymousWebView.loadUrl(paramAnonymousString);
					return false;
				}
			});
			this.wb.loadUrl("https://kj.13322.com/pk10_statistics.html");
			this.wb.setOnKeyListener(new View.OnKeyListener()
			{
				public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
				{
					if ((paramAnonymousKeyEvent.getAction() == 0) && (paramAnonymousInt == 4) && (wb.canGoBack()))
					{
						wb.goBack();
						return true;
					}
					return false;
				}
			});
			return;
		}
	}

}