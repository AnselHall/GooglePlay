package com.exe.googleplay.ui.fragment;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.exe.googleplay.http.HttpHelper;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.libs.randomlayout.StellarMap;
import com.exe.googleplay.util.ColorUtil;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.JsonUtil;
import com.exe.googleplay.util.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class RecommendFragment extends BaseFragment{
    private StellarMap stellarMap;
    private ArrayList<String> list;
    @Override
    protected View loadSuccessView() {
        stellarMap = new StellarMap(getActivity());
        stellarMap.setInnerPadding(10, 10, 10, 10);//设置随机布局的内边距
        return stellarMap;
    }

    @Override
    protected Object loadData() {

        String url = Url.RECOMMEND;
        String result = HttpHelper.get(url);
        list = (ArrayList<String>) JsonUtil.parseJsonToList(result, new TypeToken<List<String>>(){}.getType());

        if(list!=null && list.size()>0){
            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    stellarMap.setAdapter(new StellarMapAdapter());

                    stellarMap.setGroup(0, true);//设置默认显示第几组，以及释放播放缩放动画
                    stellarMap.setRegularity(10, 10);//设置x和y方向显示的密度,最好设置个中等值，不要过大，也不要过小
                }
            });
        }
        return result;
    }

    class StellarMapAdapter implements StellarMap.Adapter{

        /**
         * 总共多少组
         */
        @Override
        public int getGroupCount() {
            return 3;
        }

        /**
         * 每组多少个,在这里为11个
         */
        @Override
        public int getCount(int group) {
            return list.size()/3;
        }

        /**
         * group:当前组
         * position:当前组中的position，注意，不是list中的position
         */
        @Override
        public View getView(int group, int position, View convertView) {
            final TextView textView = new TextView(getActivity());

//			LogUtil.e(this, "当前组： "+group  +"  position: "+position);
            //根据group和position获取在list中的对应的位置
            int listPosition = group*getCount(group) + position;

            textView.setText(list.get(listPosition));

            Random random = new Random();
            //1.设置随机字体大小
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, random.nextInt(8)+14);//字体大小范围：14-21
            //2.设置随机字体颜色
            textView.setTextColor(ColorUtil.generateBeautifulColor());

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(textView.getText().toString());
                }
            });

            return textView;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        /**
         * group：当前组
         */
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            //0->1->2->0->1
            return (group+1)%3;
        }

    }
}