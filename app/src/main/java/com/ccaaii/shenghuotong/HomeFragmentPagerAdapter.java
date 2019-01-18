package com.ccaaii.shenghuotong;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ccaaii.base.log.DevLog;
import com.ccaaii.base.log.LogLevel;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private static final String TAG = "[CCAAII]EasiioHomeFragmentPagerAdapter";
	
    private List<Fragment> mFragmenstList;

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public HomeFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragmenstList = fragments;
    }

    @Override
    public int getCount() {
        return mFragmenstList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragmenstList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    
    @Override  
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);  
        if(LogLevel.DEV){
        	DevLog.d(TAG, "destroyItem position = " + position);
        }
    }  

}