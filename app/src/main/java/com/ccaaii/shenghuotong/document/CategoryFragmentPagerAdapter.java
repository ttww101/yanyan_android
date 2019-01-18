package com.ccaaii.shenghuotong.document;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccaaii.shenghuotong.R;

/**
 */
public class CategoryFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 8;
    private static final int[] CATEGORY_TITLE = new int[]{
            R.string.category_0,
            R.string.category_1,
            R.string.category_2,
            R.string.category_3,
            R.string.category_4,
            R.string.category_5,
            R.string.category_6,
            R.string.category_7
    };
    private Context mContext;

    public CategoryFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return NewCategoryFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(CATEGORY_TITLE[position]);
    }
}