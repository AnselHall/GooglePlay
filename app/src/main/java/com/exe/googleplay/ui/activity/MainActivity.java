package com.exe.googleplay.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.exe.googleplay.R;
import com.exe.googleplay.libs.pageslidingtab.PagerSlidingTab;
import com.exe.googleplay.ui.adapter.MainPagerAdapter;

public class MainActivity extends ActionBarActivity {

    private PagerSlidingTab pst_slidingtab;
    private ViewPager vp_viewpager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        pst_slidingtab = (PagerSlidingTab) findViewById(R.id.pst_slidingtab);
        vp_viewpager = (ViewPager) findViewById(R.id.vp_viewpager);
    }
    private void initData() {
//        toolbar.

//        vp_viewpager.setAdapter(new MainPagerAdapter(getSuppotrFragmentManager()));
        vp_viewpager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        pst_slidingtab.setViewPager(vp_viewpager);
    }
}
