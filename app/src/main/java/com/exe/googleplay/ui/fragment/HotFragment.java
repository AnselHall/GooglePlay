package com.exe.googleplay.ui.fragment;

import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 2016/2/22.
 */
public class HotFragment extends BaseFragment{

    @Override
    protected View loadSuccessView() {
        TextView textView = new TextView(getActivity());
        textView.setText(HotFragment.this.getClass().getSimpleName());

        return textView;
    }

    @Override
    protected Object loadData() {
        return null;
    }
}