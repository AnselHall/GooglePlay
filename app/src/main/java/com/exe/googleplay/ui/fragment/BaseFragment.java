package com.exe.googleplay.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exe.googleplay.util.LogUtil;

/**
 * Fragment的基类，完成加载数据，显示View功能
 */
public abstract class BaseFragment extends Fragment {
    private ContentPage contentPage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentPage == null) {
            contentPage = new ContentPage(getContext()) {
                @Override
                protected View createSuccessView() {
                    return loadSuccessView();
                }

                @Override
                protected Object loadData() {
                    return BaseFragment.this.loadData();
                }
            };
            LogUtil.e("tag", this.getClass().getSimpleName());
        }
        return contentPage;
    }

    protected abstract View loadSuccessView();

    protected abstract Object loadData();

}
