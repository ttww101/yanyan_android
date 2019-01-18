package com.ccaaii.shenghuotong.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccaaii.base.BaseFragment;
import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.shenghuotong.R;

public class PageOneFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    public static final String TAG = "[CCAAII]PageOneFragment";

    private static final int TOP_SLIDE_TIME = 5000;

    public PageOneFragment(){
        MarketLog.i(TAG, "HomeFragment instance");
    }

    public static PageOneFragment newInstance() {
        PageOneFragment fragment = new PageOneFragment();
        return fragment;
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

    private void buildLayout(View rootView) {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {

    }
}
