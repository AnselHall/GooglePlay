package com.exe.googleplay.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.exe.googleplay.R;
import com.exe.googleplay.ui.fragment.FragmentFactory;
import com.exe.googleplay.util.CommonUtil;

/**
 * Created by user on 2016/2/19.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private String[] tabs;
//    private ArrayList<>
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        tabs = CommonUtil.getStringArray(R.array.tab_names);
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentFactory.createFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

}
