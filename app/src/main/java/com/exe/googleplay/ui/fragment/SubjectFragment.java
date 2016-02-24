package com.exe.googleplay.ui.fragment;

import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 2016/2/22.
 */
public class SubjectFragment extends BaseFragment{

    @Override
    protected View loadSuccessView() {
        TextView textView = new TextView(getActivity());
        textView.setText(SubjectFragment.this.getClass().getSimpleName());

        return textView;
    }

    @Override
    protected Object loadData() {
        return null;
    }
}