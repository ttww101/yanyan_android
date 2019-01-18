package com.ccaaii.shenghuotong.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccaaii.base.utils.ImageLoader;
import com.ccaaii.shenghuotong.CcaaiiApp;
import com.ccaaii.shenghuotong.HomeFragmentActivity;
import com.ccaaii.shenghuotong.api.ApiUtils;
import com.ccaaii.shenghuotong.api.IResponseListener;
import com.ccaaii.shenghuotong.bean.City;
import com.ccaaii.shenghuotong.bean.DocumentBean;
import com.ccaaii.shenghuotong.bean.InitBean;
import com.ccaaii.shenghuotong.bean.Weather;
import com.ccaaii.shenghuotong.city.CityDAO;
import com.ccaaii.shenghuotong.document.DocumentDAO;
import com.ccaaii.shenghuotong.document.DocumentDetailActivity;
import com.ccaaii.shenghuotong.document.DocumentUtils;
import com.ccaaii.shenghuotong.document.NewCategoryActivity;
import com.ccaaii.shenghuotong.weather.WeatherActivity;
import com.ccaaii.shenghuotong.weather.WeatherDAO;
import com.ccaaii.base.BaseFragment;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.DateTimeUtils;
import com.ccaaii.base.utils.DensityUtils;
import com.ccaaii.base.utils.Lunar;
import com.ccaaii.base.utils.WeatherUtils;
import com.ccaaii.base.utils.widgets.FixedSpeedScroller;
import com.ccaaii.base.utils.widgets.ListViewForScrollView;
import com.ccaaii.shenghuotong.CcaaiiConstants;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.web.WebShowActivity;
import com.flurry.android.FlurryAgent;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    public static final String TAG = "[CCAAII]HomeFragment";

    private static final int TOP_SLIDE_TIME = 5000;

    private static final int NEWS_DEFAULT_COUNT = 5;

    private View mWeatherLayout;
    private TextView mCityView;
    private TextView mDateView;
    private ImageView mWeahterImageView;
    private TextView mWeatherConTmpView;
    private TextView mWeatherWindHumView;
    private TextView mWeatherQltyView;
    private TextView mWeatherAqiView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewGroup mViewGroup;
    private ListViewForScrollView mHotListView;
    private ViewPager mViewPager;
    private ImageView[] tips;
    private ImageView[] mImageViews;
    private int mCurrentTopShow = 0;
    private int mMaxTopShowIndex = 0;

    private List<InitBean.SlidePicturesBean> mTopShowList;
    private TopShowAdapter mTopShowAdapter;

    private TopShowHandler mTopShowHandler;

    private DocumentAdapter mHotDocumentAdapter;

    public HomeFragment(){
        MarketLog.i(TAG, "HomeFragment instance");
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public ImageLoader getImageLoader(){
        try {
            return ((HomeFragmentActivity) getActivity()).getImageLoader();
        } catch (Exception ex){
            return null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onAttach...");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onCreate...");
        }

        mTopShowHandler = new TopShowHandler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onCreateView...");
        }
        View v = inflater.inflate(R.layout.home_fragment_layout, container, false);
        buildLayout(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onViewCreated...");
        }

        super.onViewCreated(view, savedInstanceState);

        refreshTopShow();
        refreshHotDocument();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onResume...");
        }
        refreshWeather(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onStart...");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onPause...");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LogLevel.DEV) {
            DevLog.i(TAG, "onDestroy...");
        }

        if (mTopShowHandler != null){
            mTopShowHandler.removeMessages(WHAT_UPDATE_TOP_SHOW);
            mTopShowHandler = null;
        }

    }

    private void buildLayout(View rootView) {

        // weather
        mWeatherLayout = rootView.findViewById(R.id.weather_layout);
        mCityView = (TextView) rootView.findViewById(R.id.city_name_view);
        mDateView = (TextView) rootView.findViewById(R.id.date_view);
        mWeahterImageView = (ImageView) rootView.findViewById(R.id.weather_image_view);
        mWeatherConTmpView = (TextView) rootView.findViewById(R.id.weather_con_tmp_view);
        mWeatherWindHumView = (TextView) rootView.findViewById(R.id.weather_wind_hum_view);
        mWeatherQltyView = (TextView) rootView.findViewById(R.id.weather_qlty_view);
        mWeatherAqiView = (TextView) rootView.findViewById(R.id.weather_aqi_view);

        mHotListView = (ListViewForScrollView) rootView.findViewById(R.id.hot_document_listview);
        mHotListView.setFocusable(false);
        mHotListView.setOnItemClickListener(mHotItemClickListener);

        mViewGroup = (ViewGroup) rootView.findViewById(R.id.viewGroup);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller( mViewPager.getContext( ), 1500);
            mScroller.set( mViewPager, scroller);
        }catch(Exception e){
            if(LogLevel.MARKET){
                MarketLog.e(TAG, "Set viewpager scoller failed. e = " + e.toString());
            }
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.home_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTopShow();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.style_color, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white_color);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

        mWeatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }

    private AdapterView.OnItemClickListener mHotItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (mHotDocumentAdapter != null){
                    DocumentBean speech = mHotDocumentAdapter.getItem(position);
                    switchToDetail(speech.getId());
                }
            } catch (Exception ex){
                MarketLog.e(TAG, "mHotItemClickListener failed, ex : " + ex.getLocalizedMessage());
            }
        }
    };

    private void refreshHotDocument(){
        if (mHotListView.getChildCount() <= 0){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View footerView = inflater.inflate(R.layout.discover_footer_view, null);
            TextView seeMoreView = (TextView) footerView.findViewById(R.id.discover_see_more_view);
            seeMoreView.setText(R.string.see_more_document);
            seeMoreView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            mHotListView.addFooterView(footerView);
        }
        List<DocumentBean> hotSpeechList = DocumentDAO.getHotDocumentList(CcaaiiApp.getCcaaiiContext());
        if (mHotDocumentAdapter != null){
            mHotDocumentAdapter.notifyDocumentList(hotSpeechList);
        } else {
            mHotDocumentAdapter = new DocumentAdapter(getActivity(), hotSpeechList);
            mHotListView.setAdapter(mHotDocumentAdapter);
        }

    }

    private void switchToDetail(String objectId){
        MarketLog.i(TAG, "switch to detail screen objectId = " + objectId);
        Intent intent = new Intent(getActivity(), DocumentDetailActivity.class);
        intent.putExtra(CcaaiiConstants.EXTRA_DOCUMENT_ID, objectId);
        getActivity().startActivity(intent);
    }

    private class ViewHolder{
        public ImageView colorView;
        public TextView categoryView;
        public TextView countView;
        public TextView nameView;
        public TextView contentView;

    }

    private class DocumentAdapter extends BaseAdapter {

        private Context mContext;
        private List<DocumentBean> mDocumentList;
        private LayoutInflater mInflater;

        public DocumentAdapter(Context context, List<DocumentBean> list){
            this.mContext = context;
            this.mDocumentList = list;
            mInflater = LayoutInflater.from(context);
        }

        public void notifyDocumentList(List<DocumentBean> list){
            this.mDocumentList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDocumentList != null ? mDocumentList.size() : 0;
        }

        @Override
        public DocumentBean getItem(int position) {
            return mDocumentList != null && position < mDocumentList.size() ? mDocumentList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.document_item_layout, null);
                viewHolder.colorView = (ImageView) convertView.findViewById(R.id.color_imageview);
                viewHolder.categoryView = (TextView) convertView.findViewById(R.id.category_textview);
                viewHolder.countView = (TextView) convertView.findViewById(R.id.count_textview);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.name_textview);
                viewHolder.contentView = (TextView) convertView.findViewById(R.id.content_textview);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            DocumentBean document = mDocumentList.get(position);
            if (document == null){
                return null;
            }

            viewHolder.colorView.setImageResource(DocumentUtils.getCategoryColorImg(document.getCategory()));
            viewHolder.categoryView.setText(DocumentUtils.getCategoryStr(mContext, document.getCategory()));
            viewHolder.countView.setText(DocumentUtils.getPlayCountStr(mContext, document.getPlaycount()));
            viewHolder.nameView.setText(document.getName());
            viewHolder.contentView.setText(document.getDescription());

            return convertView;
        }
    }



    public class TopShowAdapter extends PagerAdapter {

        public TopShowAdapter(){
        }

        @Override
        public int getCount() {
            return mTopShowList != null ? mTopShowList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // ((ViewPager)container).removeView(mImageViews[position %
            // mImageViews.length]);

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                container.addView(mImageViews[position]);
                final InitBean.SlidePicturesBean topShow = mTopShowList.get(position);
                String fileUrl = topShow.getImage();
                if (getImageLoader() != null){
                    getImageLoader().displayImage(fileUrl, mImageViews[position]);
                    try {
                        ((HomeFragmentActivity) getActivity()).addKeyString(fileUrl);
                    } catch (Exception ex){}

                }

                mImageViews[position].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(getActivity(), WebShowActivity.class);
                            intent.putExtra(CcaaiiConstants.EXTRA_WEB_TITLE, "");
                            intent.putExtra(CcaaiiConstants.EXTRA_WEB_URL, topShow.getTarget());
                            getActivity().startActivity(intent);
                            FlurryAgent.logEvent("android_click_ad_"+topShow.getTarget());


                        } catch (Exception ex){

                        }
                    }
                });
            } catch (Exception e) {
            }
            return mImageViews[position];
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentTopShow = position;
        setImageBackground(position);
    }

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.icon_point_selected);
            } else {
                tips[i].setBackgroundResource(R.drawable.icon_point_nor);
            }
        }
    }

    private void refreshTopShow(){
        if (getActivity().isFinishing()){
            return;
        }

        InitBean initBean = CcaaiiApp.getInitBean();
        if(initBean==null) {
            MarketLog.e(TAG,"initBean is null");
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        mTopShowList = initBean.getSlidePictures();
        if(mTopShowList == null || mTopShowList.size() <= 0){
            mViewGroup.removeAllViews();
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        mCurrentTopShow = 0;
        mMaxTopShowIndex = mTopShowList.size();
        mViewGroup.removeAllViews();
        tips = new ImageView[mTopShowList.size()];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(5, 5));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.icon_point_selected);
            } else {
                tips[i].setBackgroundResource(R.drawable.icon_point_nor);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(DensityUtils.dp_px(7), DensityUtils.dp_px(7)));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            mViewGroup.addView(imageView, layoutParams);
        }

        mImageViews = new ImageView[mTopShowList.size()];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundColor(this.getResources().getColor(R.color.transparent_full));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViews[i] = imageView;
        }

        if (mTopShowAdapter == null){
            mTopShowAdapter = new TopShowAdapter();
            mViewPager.setAdapter(mTopShowAdapter);
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setOffscreenPageLimit(mMaxTopShowIndex);
            mViewPager.setCurrentItem(0);
        } else {
            mTopShowAdapter.notifyDataSetChanged();
        }


        mSwipeRefreshLayout.setRefreshing(false);
        startToRunTopShow();
    }

    private static final int WHAT_UPDATE_TOP_SHOW = 1;
    private class TopShowHandler extends Handler{

        @Override
        public void handleMessage(Message msg){
            if(msg.what == WHAT_UPDATE_TOP_SHOW){
                mCurrentTopShow ++;
                if(mCurrentTopShow >= mMaxTopShowIndex){
                    mCurrentTopShow = 0;
                }
                mViewPager.setCurrentItem(mCurrentTopShow, true);
                startToRunTopShow();
            }
        }
    }

    private void startToRunTopShow(){
        mTopShowHandler.removeMessages(WHAT_UPDATE_TOP_SHOW);
        Message msg = mTopShowHandler.obtainMessage(WHAT_UPDATE_TOP_SHOW);
        mTopShowHandler.sendMessageDelayed(msg, TOP_SLIDE_TIME);
    }

    public void refreshWeather(boolean fromLocation){
        if (getActivity().isFinishing()){
            return;
        }

        String cityId = "";
        String cityStr = "";
        String provinceStr = "";
        Weather currentWeather = WeatherDAO.getCurrentWeather(getActivity());
        if (currentWeather != null){
            cityId = currentWeather.cityId;
            cityStr = currentWeather.city;
            provinceStr = currentWeather.province;
            updateWeatherView(currentWeather);
            if (System.currentTimeMillis() - currentWeather.updateTime <= 1800000 && !TextUtils.isEmpty(currentWeather.weather)){
                MarketLog.w(TAG, "Current time is : " + System.currentTimeMillis() + ", last weather time is : " + currentWeather.updateTime);
                return;
            }
        }

        if (TextUtils.isEmpty(cityId)){
            if (TextUtils.isEmpty(CcaaiiApp.getCity()) && fromLocation){
                mCityView.setText(R.string.location_failed);
                return;
            }
            City city = CityDAO.getCityByCityAndProv(getActivity(), CcaaiiApp.getProvince(), CcaaiiApp.getCity());
            if (city != null){
                cityId = city.id;
                cityStr = city.city;
                provinceStr = city.prov;
            } else {
                mCityView.setText(R.string.init_location);
                return;
            }
        }

        MarketLog.w(TAG, "refreshWeather cityId = " + cityId + ", city = " + cityStr + ", province = " + provinceStr);

        ApiUtils.getWeather(getActivity(), cityId, cityStr, provinceStr, true, new IResponseListener() {
            @Override
            public void onResponseResult(boolean success, int code, String msg) {
                if (success){
                    Weather currentWeather = WeatherDAO.getCurrentWeather(getActivity());
                    updateWeatherView(currentWeather);
                }
            }
        });
    }

    private void updateWeatherView(Weather weather){
        if (weather == null){
            return;
        }

        mCityView.setText(weather.city);
        Date d = new Date(System.currentTimeMillis());
        String date = DateTimeUtils.getDateLabel(d);
        try {
            date = date + " " + this.getString(DateTimeUtils.getWeekdayByStrDate(date));
            Calendar calendar = Calendar.getInstance();
            Lunar lunar = new Lunar(calendar);
            String lunarStr = " ";
            lunarStr = lunar.cyclical() + lunar.animalsYear()+"年 ";
            lunarStr +=lunar.toString2();
            date = date + " " + lunarStr;
        } catch (Exception ex){

        }
        mDateView.setText(date);

        try {
            WeatherUtils.WEATHER_RES weatherRes = WeatherUtils.WEATHER_RES.getWeatherResByCode(weather.now.cond_code);
            mWeatherLayout.setBackgroundResource(weatherRes.getWeatherBgId());
            mWeahterImageView.setImageResource(weatherRes.getWeatherImgId());
            mWeatherConTmpView.setText(getActivity().getString(R.string.weather_con_tmp, weather.now.cond_txt, weather.now.tmp));
            mWeatherWindHumView.setText(getActivity().getString(R.string.weather_wind_hum, weather.now.wind_dir, weather.now.wind_sc, weather.now.hum) + "%");
            mWeatherQltyView.setText(this.getString(R.string.weather_show_qlty, weather.now.pres));
            mWeatherAqiView.setText(this.getString(R.string.weather_show_aqi, weather.now.vis));
        } catch (Exception ex){
            MarketLog.e(TAG, "updateWeatherView failed, ex : " + ex.getLocalizedMessage());
            return;
        }

    }

}
