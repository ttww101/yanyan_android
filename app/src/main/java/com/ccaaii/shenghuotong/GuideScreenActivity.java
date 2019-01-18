package com.ccaaii.shenghuotong;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ccaaii.shenghuotong.api.IResponseListener;
import com.ccaaii.shenghuotong.bean.DocumentBean;
import com.ccaaii.shenghuotong.document.DocumentDAO;
import com.google.gson.reflect.TypeToken;
import com.ccaaii.shenghuotong.api.ApiUtils;
import com.ccaaii.shenghuotong.bean.CityInfo;
import com.ccaaii.shenghuotong.city.CityDAO;
import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.AnimationUtils;
import com.ccaaii.base.utils.CharUtils;

public class GuideScreenActivity extends BaseActivity{

	private static final String TAG = "[CCAAII]GuideScreenActivity";
	
	private ViewPager mViewPager;  
    private ArrayList<View> mViewList;  
    private ImageView[] mImageViews;
    private int mViewCount;
    private int mCurSel;
    private LinearLayout mBottomDotLayout;
    private Button mStartButton;
    
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(LogLevel.MARKET){
			MarketLog.i(TAG, "onCreate...");
		}

		this.setContentView(R.layout.first_initiate_layout, false);

		SharedPreferences setting = this.getSharedPreferences(CcaaiiConstants.CCAAII_SHARED_PREFERENCES_CONSTANTS, 0);
		setting.edit().putBoolean(CcaaiiConstants.FIRST_INSTALL_FLAG, false).commit();

		LayoutInflater inflater = getLayoutInflater();  
        mViewList = new ArrayList<View>();  
        mViewList.add(inflater.inflate(R.layout.first_initiate_layout_00, null));
		mViewList.add(inflater.inflate(R.layout.first_initiate_layout_02, null));
		mViewList.add(inflater.inflate(R.layout.first_initiate_layout_01, null));
  
         
        mStartButton = (Button) this.findViewById(R.id.start_app_button);
        mStartButton.setVisibility(View.GONE);
        mStartButton.setOnClickListener(mStartOnClickListener);
        mViewPager = (ViewPager) this.findViewById(R.id.guide_view_pager);  
  
        mBottomDotLayout = (LinearLayout) findViewById(R.id.bottom_dot_layout);
        if(mBottomDotLayout == null){
        	MarketLog.e(TAG, "onCreate bottom dot layout is null");
        	return;
        }
        mViewCount = mViewList.size();
        mImageViews = new ImageView[mViewCount];

		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (ImageView) mBottomDotLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setTag(i);
		}
		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);
		
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new GuidePageAdapter());  
        mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

		initProvinceCityDB();
		initDocumentsDB();


	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mCurSel = index;
		
		if (index >= 2){
			AnimationUtils.alphaInOrOut(mStartButton, true, 500);
		} else {
			mStartButton.setVisibility(View.GONE);
		}
	}

    class GuidePageAdapter extends PagerAdapter {  
    	  
    	@Override  
        public Object instantiateItem(ViewGroup container, int position)  
        {  

            container.addView(mViewList.get(position));  
            return mViewList.get(position);  
        }  

        @Override  
        public void destroyItem(ViewGroup container, int position,  
                Object object)  
        {  

            container.removeView(mViewList.get(position));  
        }  

        @Override  
        public boolean isViewFromObject(View view, Object object)  
        {  
            return view == object;  
        }  

        @Override  
        public int getCount()  
        {  
            return mViewList.size();  
        } 
    } 
    
    class GuidePageChangeListener implements OnPageChangeListener {  
  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
        	setCurPoint(arg0);
        }  
  
    } 

    private View.OnClickListener mStartOnClickListener = new View.OnClickListener() {
	    
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(GuideScreenActivity.this, HomeFragmentActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			GuideScreenActivity.this.startActivity(i);
			GuideScreenActivity.this.finish();
		}
	};


	private void initProvinceCityDB(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
				MarketLog.w(TAG, "Start init city");
                String cityInfoStr = CharUtils.getFromAssets(CcaaiiApp.getCcaaiiContext(), "cncity.json");
                CityInfo cityInfo = CcaaiiApp.getGson().fromJson(cityInfoStr,  new TypeToken<CityInfo>() {}.getType());
				CityDAO.saveCity(CcaaiiApp.getCcaaiiContext(), cityInfo);
				MarketLog.w(TAG, "End init city");
            }
        });
        thread.start();
    }

	private void initDocumentsDB(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				MarketLog.w(TAG, "Start init documents");
				String str = CharUtils.getFromAssets(CcaaiiApp.getCcaaiiContext(), "baike.json");
				List<DocumentBean> documentList = CcaaiiApp.getGson().fromJson(str,  new TypeToken<List<DocumentBean>>() {}.getType());
				DocumentDAO.saveDocumentList(CcaaiiApp.getCcaaiiContext(), documentList);
				MarketLog.w(TAG, "End init documents");
			}
		});
		thread.start();
	}

}
