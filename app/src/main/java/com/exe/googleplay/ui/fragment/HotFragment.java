package com.exe.googleplay.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.exe.googleplay.http.HttpHelper;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.ui.view.FlowLayout;
import com.exe.googleplay.util.ColorUtil;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.DrawableUtil;
import com.exe.googleplay.util.JsonUtil;
import com.exe.googleplay.util.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HotFragment extends BaseFragment{

    private ArrayList<String> list = new ArrayList<>();
    private ScrollView scrollView;
    private FlowLayout flowLayout;
    @Override
    protected View loadSuccessView() {
        scrollView = new ScrollView(getActivity());
        flowLayout = new FlowLayout(getActivity());
        flowLayout.setPadding(12, 12, 12, 12);
        flowLayout.setVerticalSpacing(15);
        scrollView.addView(flowLayout);
        return scrollView;
    }

    @Override
    protected Object loadData() {
        String url = Url.HOT;
        String result = HttpHelper.get(url);

        list = (ArrayList<String>) JsonUtil.parseJsonToList(result,new TypeToken<List<String>>(){}.getType());
        if(list!=null && list.size()>0){
            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < list.size(); i++) {
                        final TextView textView = new TextView(getActivity());
                        textView.setPadding(10, 5, 10, 5);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(Color.WHITE);
                        Drawable normal = DrawableUtil.generateDrawable(ColorUtil.generateBeautifulColor());
                        Drawable pressed = DrawableUtil.generateDrawable(Color.GRAY);
                        textView.setBackgroundDrawable(DrawableUtil.generateSelector(pressed, normal));

                        textView.setText(list.get(i));

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showToast(textView.getText().toString());
                            }
                        });
                        flowLayout.addView(textView);
                    }
                }
            });
        }
        return list;
    }
}