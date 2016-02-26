package com.exe.googleplay.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.exe.googleplay.R;
import com.exe.googleplay.global.GooglePlayApplication;
import com.exe.googleplay.global.ImageLoaderCfg;
import com.exe.googleplay.http.Url;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 *
 */
public class HomeHeaderAdapter extends PagerAdapter {
    private ArrayList<String> list;

    public HomeHeaderAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override

    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView view = (ImageView) View.inflate(GooglePlayApplication.getContext(), R.layout.adapter_home_header, null);

        ImageLoader.getInstance().displayImage(Url.HOST+list.get(position), view, ImageLoaderCfg.default_options);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
