package com.ccaaii.shenghuotong.document;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;
import com.ccaaii.base.utils.StatusBarUtils;
import com.ccaaii.base.utils.widgets.FixedSpeedScroller;
import com.ccaaii.shenghuotong.CcaaiiConstants;
import com.ccaaii.shenghuotong.R;
import com.ccaaii.shenghuotong.bean.DocumentBean;

import java.lang.reflect.Field;

/**
 */
public class NewCategoryActivity extends FragmentActivity {

    private static final String TAG = "[CCAAII]NewCategoryActivity";

    private static final String CATEGORY_POSITION = "CATEGORY_POSITION";



    private int mCurrentCategory;

    private CategoryFragmentPagerAdapter mCategoryFragmentPagerAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MarketLog.d(TAG, "onCreate");
        mCurrentCategory = DocumentBean.CATEGORY_COMMON;
        if (getIntent() != null){
            mCurrentCategory = getIntent().getIntExtra(CcaaiiConstants.EXTRA_CATEGORY_ID, DocumentBean.CATEGORY_COMMON);
        }
        this.setContentView(R.layout.category_layout);
        StatusBarUtils.setColor(this, this.getResources().getColor(R.color.style_color));

        buildLayout();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CATEGORY_POSITION, mTabLayout.getSelectedTabPosition());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(CATEGORY_POSITION));
    }

    private void buildLayout(){
        this.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTabLayout = (TabLayout) this.findViewById(R.id.category_tablayout);
        mViewPager = (ViewPager) this.findViewById(R.id.category_viewpager);
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

        mCategoryFragmentPagerAdapter = new CategoryFragmentPagerAdapter(this, this.getSupportFragmentManager());
        mViewPager.setAdapter(mCategoryFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mViewPager.setCurrentItem(mCurrentCategory);
    }

}
