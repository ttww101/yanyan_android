package com.ccaaii.shenghuotong.weather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccaaii.shenghuotong.CcaaiiApp;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ccaaii.base.utils.DateTimeUtils;
import com.ccaaii.base.utils.widgets.WeatherItemLayout;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.api.ApiUtils;
import com.ccaaii.shenghuotong.api.IResponseListener;
import com.ccaaii.shenghuotong.bean.City;
import com.ccaaii.shenghuotong.bean.Weather;
import com.ccaaii.shenghuotong.city.CityActivity;
import com.ccaaii.shenghuotong.city.CityDAO;
import com.ccaaii.base.BaseActivity;
import com.ccaaii.base.config.Build;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.WeatherUtils;
import com.ccaaii.shenghuotong.provider.CcaaiiProviderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class WeatherActivity extends BaseActivity{

    private static final String TAG = "[CCAAII]WeatherActivity";

    private static final int WEATHER_LENGTH = 6;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTitleView;
    private LinearLayout mBgLayout;
    private ImageView mWeahterImageView;
    private TextView mWeatherUpdateTimeView;
    private TextView mWeatherTmpView;
    private TextView mWeatherConView;
    private TextView mWeatherWindView;
    private TextView mWeatherHumView;
    private TextView mWeatherQltyView;
    private TextView mWeatherAqiView;

    private TextView mDrsgBrfView;
    private TextView mDrsgTxtView;
    private TextView mComfBrfView;
    private TextView mComfTxtView;
    private TextView mSportBrfView;
    private TextView mSportTxtView;
    private TextView mTravBrfView;
    private TextView mTravTxtView;
    private TextView mUvBrfView;
    private TextView mUvTxtView;
    private TextView mCwBrfView;
    private TextView mCwTxtView;
    private TextView mFluBrfView;
    private TextView mFluTxtView;

    private WeatherItemLayout[] mTopDailyLayout = new WeatherItemLayout[WEATHER_LENGTH];
    private WeatherItemLayout[] mBottomDailyLayout = new WeatherItemLayout[WEATHER_LENGTH];

    private LineChart mLineChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.weather_layout, true);

        buildLayout();

    }

    @Override
    public void onResume() {
        super.onResume();

        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void buildLayout(){
        mTitleView = (TextView) this.findViewById(R.id.title_view);
        this.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.findViewById(R.id.button_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, CityActivity.class);
                WeatherActivity.this.startActivity(intent);
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.weather_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.style_color, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.high_white); // 设定下拉圆圈的背景
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小

        mBgLayout = (LinearLayout) this.findViewById(R.id.weather_bg_layout);
        mWeahterImageView = (ImageView) this.findViewById(R.id.weather_image_view);
        mWeatherUpdateTimeView = (TextView) this.findViewById(R.id.weather_update_time_view);
        mWeatherTmpView = (TextView) this.findViewById(R.id.weather_tmp_view);
        mWeatherConView = (TextView) this.findViewById(R.id.weather_con_view);
        mWeatherWindView = (TextView) this.findViewById(R.id.weather_wind_view);
        mWeatherHumView = (TextView) this.findViewById(R.id.weather_hum_view);
        mWeatherQltyView = (TextView) this.findViewById(R.id.weather_qlty_view);
        mWeatherAqiView = (TextView) this.findViewById(R.id.weather_aqi_view);

        mTopDailyLayout[0] = (WeatherItemLayout) this.findViewById(R.id.top_daily_item_layout0);
        mTopDailyLayout[1] = (WeatherItemLayout) this.findViewById(R.id.top_daily_item_layout1);
        mTopDailyLayout[2] = (WeatherItemLayout) this.findViewById(R.id.top_daily_item_layout2);
        mTopDailyLayout[3] = (WeatherItemLayout) this.findViewById(R.id.top_daily_item_layout3);
        mTopDailyLayout[4] = (WeatherItemLayout) this.findViewById(R.id.top_daily_item_layout4);
        mTopDailyLayout[5] = (WeatherItemLayout) this.findViewById(R.id.top_daily_item_layout5);
        mBottomDailyLayout[0] = (WeatherItemLayout) this.findViewById(R.id.bottom_daily_item_layout0);
        mBottomDailyLayout[1] = (WeatherItemLayout) this.findViewById(R.id.bottom_daily_item_layout1);
        mBottomDailyLayout[2] = (WeatherItemLayout) this.findViewById(R.id.bottom_daily_item_layout2);
        mBottomDailyLayout[3] = (WeatherItemLayout) this.findViewById(R.id.bottom_daily_item_layout3);
        mBottomDailyLayout[4] = (WeatherItemLayout) this.findViewById(R.id.bottom_daily_item_layout4);
        mBottomDailyLayout[5] = (WeatherItemLayout) this.findViewById(R.id.bottom_daily_item_layout5);
        mLineChart = (LineChart) this.findViewById(R.id.weather_chart);

        mDrsgBrfView = (TextView) this.findViewById(R.id.weather_suggestion_drsg_brf);
        mDrsgTxtView = (TextView) this.findViewById(R.id.weather_suggestion_drsg_txt);
        mComfBrfView = (TextView) this.findViewById(R.id.weather_suggestion_comf_brf);
        mComfTxtView = (TextView) this.findViewById(R.id.weather_suggestion_comf_txt);
        mSportBrfView = (TextView) this.findViewById(R.id.weather_suggestion_sport_brf);
        mSportTxtView = (TextView) this.findViewById(R.id.weather_suggestion_sport_txt);
        mTravBrfView = (TextView) this.findViewById(R.id.weather_suggestion_trav_brf);
        mTravTxtView = (TextView) this.findViewById(R.id.weather_suggestion_trav_txt);
        mUvBrfView = (TextView) this.findViewById(R.id.weather_suggestion_uv_brf);
        mUvTxtView = (TextView) this.findViewById(R.id.weather_suggestion_uv_txt);
        mCwBrfView = (TextView) this.findViewById(R.id.weather_suggestion_cw_brf);
        mCwTxtView = (TextView) this.findViewById(R.id.weather_suggestion_cw_txt);
        mFluBrfView = (TextView) this.findViewById(R.id.weather_suggestion_flu_brf);
        mFluTxtView = (TextView) this.findViewById(R.id.weather_suggestion_flu_txt);
    }

    private void initView(){
        String cityId = "";
        String cityStr = "";
        String provinceStr = "";
        Weather currentWeather = WeatherDAO.getCurrentWeather(this);
        if (currentWeather != null){
            cityId = currentWeather.cityId;
            cityStr = currentWeather.city;
            provinceStr = currentWeather.province;
            updateWeatherView(currentWeather);
            if (System.currentTimeMillis() - currentWeather.updateTime <= 1800000 && !TextUtils.isEmpty(currentWeather.weather)){
                MarketLog.w(TAG, "Current time is : " + System.currentTimeMillis() + ", last weather time is : " + currentWeather.updateTime);
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
        }

        if (TextUtils.isEmpty(cityId)){
            if (TextUtils.isEmpty(CcaaiiApp.getCity())){
                mTitleView.setText(R.string.location_failed);
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            City city = CityDAO.getCityByCityAndProv(this, CcaaiiApp.getProvince(), CcaaiiApp.getCity());
            if (city != null){
                cityId = city.id;
                cityStr = city.city;
                provinceStr = city.prov;
            } else {
                mTitleView.setText(R.string.init_location);
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
        }

        MarketLog.w(TAG, "refreshWeather cityId = " + cityId + ", city = " + cityStr + ", province = " + provinceStr);

        ApiUtils.getWeather(this, cityId, cityStr, provinceStr, true, new IResponseListener() {
            @Override
            public void onResponseResult(boolean success, int code, String msg) {
                if (success){
                    mSwipeRefreshLayout.setRefreshing(false);
                    Weather currentWeather = WeatherDAO.getCurrentWeather(WeatherActivity.this);
                    updateWeatherView(currentWeather);
                }
            }
        });


    }

    private void updateWeatherView(Weather weather) {
        MarketLog.d(TAG, "updateWeatherView");
        if (weather == null) {
            MarketLog.w(TAG, "updateWeatherView weather is null");
            return;
        }

        mTitleView.setText(weather.city);
        try {
            if (weather.now != null){
                WeatherUtils.WEATHER_RES weatherRes = WeatherUtils.WEATHER_RES.getWeatherResByCode(weather.now.cond_code);
                mBgLayout.setBackgroundResource(weatherRes.getWeatherBigBgId());
                mWeahterImageView.setImageResource(weatherRes.getWeatherImgId());
                mWeatherTmpView.setText(weather.now.tmp + "℃");
                mWeatherConView.setText(weather.now.cond_txt);
                mWeatherWindView.setText(this.getString(R.string.weather_show_wind, weather.now.wind_dir, weather.now.wind_sc));
                mWeatherHumView.setText(this.getString(R.string.weather_show_hum, weather.now.hum) + "%");
                mWeatherQltyView.setText(this.getString(R.string.weather_show_qlty, weather.now.pres));
                mWeatherAqiView.setText(this.getString(R.string.weather_show_aqi, weather.now.vis));

                String updateTime = "";
                if(weather.basic != null){
                    if (weather.update != null){
                        updateTime = weather.update.loc;
                    }
                }
                mWeatherUpdateTimeView.setText(this.getString(R.string.weather_show_update_time, updateTime));
            }

            for (Weather.LifestyleBean lifestyle : weather.getLifestyle()){
                if (lifestyle != null){
                    if (lifestyle.type.equals("comf")){
                        mComfBrfView.setText(lifestyle.brf);
                        mComfTxtView.setText(lifestyle.txt);
                    } else if (lifestyle.type.equals("drsg")){
                        mDrsgBrfView.setText(lifestyle.brf);
                        mDrsgTxtView.setText(lifestyle.txt);
                    } else if (lifestyle.type.equals("sport")){
                        mSportBrfView.setText(lifestyle.brf);
                        mSportTxtView.setText(lifestyle.txt);
                    } else if (lifestyle.type.equals("flu")){
                        mFluBrfView.setText(lifestyle.brf);
                        mFluTxtView.setText(lifestyle.txt);
                    } else if (lifestyle.type.equals("trav")){
                        mTravBrfView.setText(lifestyle.brf);
                        mTravTxtView.setText(lifestyle.txt);
                    } else if (lifestyle.type.equals("uv")){
                        mUvBrfView.setText(lifestyle.brf);
                        mUvTxtView.setText(lifestyle.txt);
                    } else if (lifestyle.type.equals("cw")){
                        mCwBrfView.setText(lifestyle.brf);
                        mCwTxtView.setText(lifestyle.txt);
                    }
                }

            }

            if (weather.daily_forecast == null || weather.daily_forecast.size() < 3){
                mLineChart.setVisibility(View.GONE);
                return;
            }

            mLineChart.clear();
            mLineChart.setVisibility(View.VISIBLE);

            int count = weather.daily_forecast.size();
            if (count > WEATHER_LENGTH){
                count = WEATHER_LENGTH;
            }
            int max = 0;
            int min = 50;

            for (int i = 0; i < count; i ++){
                Weather.DailyForecastBean daily = weather.daily_forecast.get(i);

                if (i == 0){
                    mTopDailyLayout[i].setData(R.string.weather_today, daily.cond_txt_d, daily.cond_code_d);
                } else {
                    mTopDailyLayout[i].setData(daily.date.substring(5), daily.cond_txt_d, daily.cond_code_d);
                }
                mBottomDailyLayout[i].setData(DateTimeUtils.getWeekdayByStrDate(daily.date), daily.cond_txt_n, daily.cond_code_n);

                if (Integer.valueOf(daily.tmp_max) > max){
                    max = Integer.valueOf(daily.tmp_max);
                }

                if (Integer.valueOf(daily.tmp_min) < min){
                    min = Integer.valueOf(daily.tmp_min);
                }
            }

            for (int i = 0; i < WEATHER_LENGTH; i ++){
                if (i >= count){
                    mTopDailyLayout[i].setVisibility(View.GONE);
                    mBottomDailyLayout[i].setVisibility(View.GONE);
                } else {
                    mTopDailyLayout[i].setVisibility(View.VISIBLE);
                    mBottomDailyLayout[i].setVisibility(View.VISIBLE);
                }
            }

            max += 2;
            min -= 2;

            setChart(mLineChart);


            Legend l = mLineChart.getLegend();
            l.setEnabled(false);
//            l.setForm(Legend.LegendForm.LINE);
//            l.setTextSize(12f);
//            l.setTextColor(Color.TRANSPARENT);
//            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setSpaceBetweenLabels(1);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawLabels(false);

            YAxis leftAxis = mLineChart.getAxisLeft();
            setYAxisLeft(leftAxis, max, min);

            YAxis rightAxis = mLineChart.getAxisRight();
            setYAxisRight(rightAxis, max, min);

            setData(mLineChart, weather.daily_forecast);
            mLineChart.invalidate();
        } catch (Exception ex){
            MarketLog.e(TAG, "updateWeatherView failed, ex : " + ex.getLocalizedMessage());
            return;
        }

    }


    private void setChart(LineChart mChart) {
        mChart.setDescription("");
        mChart.setNoDataTextDescription("Empty data");
        mChart.setTouchEnabled(false);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBorders(false);
        mChart.setDrawMarkerViews(false);
        mChart.setHighlightPerDragEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.animateX(800);
    }

    private void setYAxisLeft(YAxis leftAxis, int max, int min) {
        MarketLog.w(TAG, "setYAxisLeft max = " + max + ", min = " + min);
        leftAxis.setLabelCount(0, false);
        // Y坐标轴轴线的颜色
        leftAxis.setGridColor(Color.TRANSPARENT);
        // Y轴坐标轴上坐标刻度值的颜色
        leftAxis.setTextColor(Color.TRANSPARENT);
        // Y坐标轴最大值
        leftAxis.setAxisMaxValue(max);
        // Y坐标轴最小值
        leftAxis.setAxisMinValue(min);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
    }

    private void setYAxisRight(YAxis rightAxis, int max, int min) {
        rightAxis.setLabelCount(0, false);
        rightAxis.setTextColor(Color.TRANSPARENT);
        rightAxis.setGridColor(Color.TRANSPARENT);
        rightAxis.setAxisMaxValue(max);
        rightAxis.setAxisMinValue(min);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
    }

    private void setData(LineChart mChart, List<Weather.DailyForecastBean> dailyList) {

        if (dailyList == null || dailyList.size() <= 0){
            return;
        }
        int count = dailyList.size();
        if (count > WEATHER_LENGTH){
            count = WEATHER_LENGTH;
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(dailyList.get(i).date);
        }

        ArrayList<Entry> yHigh = new ArrayList<Entry>();
        LineDataSet high = new LineDataSet(yHigh, "高温");
        setHighTemperature(high, yHigh, dailyList);

        ArrayList<Entry> yLow = new ArrayList<Entry>();
        LineDataSet low = new LineDataSet(yLow, "低温");
        setLowTemperature(low, yLow, dailyList);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(high);
        dataSets.add(low);

        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(14f);
        mChart.setData(data);
    }

    private void setHighTemperature(LineDataSet high, ArrayList<Entry> yVals,
                                    List<Weather.DailyForecastBean> dailyList) {

        int count = dailyList.size();
        if (count > WEATHER_LENGTH){
            count = WEATHER_LENGTH;
        }
        for (int i = 0; i < count; i++) {
            yVals.add(new Entry(Float.valueOf(dailyList.get(i).tmp_max), i));
        }

        // 以左边的Y坐标轴为准
        high.setAxisDependency(YAxis.AxisDependency.LEFT);
        high.setLineWidth(2f);
        high.setColor(this.getResources().getColor(R.color.high_tmp_line_color));
        high.setCircleRadius(5f);
        high.setCircleColor(this.getResources().getColor(R.color.high_tmp_line_color));
        high.setDrawCircleHole(false);

        high.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return ((int)value) + "°";
            }

        });

    }

    private void setLowTemperature(LineDataSet low, ArrayList<Entry> yVals, List<Weather.DailyForecastBean> dailyList) {
        int count = dailyList.size();
        if (count > WEATHER_LENGTH){
            count = WEATHER_LENGTH;
        }
        for (int i = 0; i < count; i++) {
            yVals.add(new Entry(Float.valueOf(dailyList.get(i).tmp_min), i));
        }

        low.setAxisDependency(YAxis.AxisDependency.LEFT);
        low.setColor(this.getResources().getColor(R.color.low_tmp_line_color));
        low.setLineWidth(2f);
        low.setCircleColor(this.getResources().getColor(R.color.low_tmp_line_color));
        low.setCircleRadius(5f);
        low.setDrawCircleHole(false);

        low.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return ((int)value) + "°";
            }


        });
    }

}
