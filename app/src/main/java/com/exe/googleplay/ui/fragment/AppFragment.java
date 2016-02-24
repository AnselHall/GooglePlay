package com.exe.googleplay.ui.fragment;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2016/2/22.
 */
public class AppFragment extends BaseFragment{

    @Override
    protected View loadSuccessView() {
        TextView textView = new TextView(getActivity());
        textView.setText(AppFragment.this.getClass().getSimpleName());

        return textView;
    }

    @Override
    protected Object loadData() {

        return new ArrayList<String>();
    }
}
