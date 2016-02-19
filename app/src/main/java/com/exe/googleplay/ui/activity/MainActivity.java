package com.exe.googleplay.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.exe.googleplay.R;
import com.exe.googleplay.lib.pagerslidingtab.PagerSlidingTab;

public class MainActivity extends Activity {

    private PagerSlidingTab pst_slidingtab;
    private ViewPager vp_viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }


    private void initView() {
        pst_slidingtab = (PagerSlidingTab) findViewById(R.id.pst_slidingtab);
        vp_viewpager = (ViewPager) findViewById(R.id.vp_viewpager);
    }
    private void initData() {
//        vp_viewpager.setAdapter(new MainPagerAdapter());
    }
}
