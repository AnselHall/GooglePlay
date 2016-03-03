package com.exe.googleplay.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.CategoryInfo;
import com.exe.googleplay.global.ImageLoaderCfg;
import com.exe.googleplay.http.Url;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 *
 */
public class CategoryAdapter extends BasicAdapter<Object> {
    private Context context;
    private ArrayList<Object> list;

    public CategoryAdapter(Context context, ArrayList<Object> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    private final int ITEM_TITLE = 0;//title类型的item
    private final int ITEM_INFO = 1;//info类型的item

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = list.get(position);
        if (object instanceof String) {
            //当前是title类型的item
            return ITEM_TITLE;
        } else {
            //当前是info类型的item
            return ITEM_INFO;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ITEM_TITLE://当前是title
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.adapter_category_title, null);
                }
                CategoryTitleHolder titleHolder = CategoryTitleHolder.getHolder(convertView);
                String title = (String) list.get(position);
                titleHolder.tv_title.setText(title);
                break;
            case ITEM_INFO://当前是info
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.adapter_category_info, null);
                }
                CategoryInfoHolder infoHolder = CategoryInfoHolder.getHolder(convertView);
                CategoryInfo info = (CategoryInfo) list.get(position);

                infoHolder.tv_name1.setText(info.getName1());
                ImageLoader.getInstance().displayImage(Url.HOST + info.getUrl1(), infoHolder.iv_image1, ImageLoaderCfg.default_options);
                //显示2和3的时候都要判断是否为空
                if (!TextUtils.isEmpty(info.getName2())) {
                    //因为是复用的convertView，所以要重置为显示
                    infoHolder.ll_info2.setVisibility(View.VISIBLE);
                    infoHolder.tv_name2.setText(info.getName2());
                    ImageLoader.getInstance().displayImage(Url.HOST + info.getUrl2(), infoHolder.iv_image2, ImageLoaderCfg.default_options);
                } else {
                    //隐藏2
                    infoHolder.ll_info2.setVisibility(View.INVISIBLE);
                }

                if (!TextUtils.isEmpty(info.getName3())) {
                    //因为是复用的convertView，所以要重置为显示
                    infoHolder.ll_info3.setVisibility(View.VISIBLE);
                    infoHolder.tv_name3.setText(info.getName3());
                    ImageLoader.getInstance().displayImage(Url.HOST + info.getUrl3(), infoHolder.iv_image3, ImageLoaderCfg.default_options);
                } else {
                    //隐藏2
                    infoHolder.ll_info3.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return convertView;
    }

    /**
     * info对应的holder
     *
     * @author Administrator
     */
    static class CategoryInfoHolder {
        ImageView iv_image1, iv_image2, iv_image3;
        TextView tv_name1, tv_name2, tv_name3;
        LinearLayout ll_info2, ll_info3;

        public CategoryInfoHolder(View convertView) {
            iv_image1 = (ImageView) convertView.findViewById(R.id.iv_image1);
            iv_image2 = (ImageView) convertView.findViewById(R.id.iv_image2);
            iv_image3 = (ImageView) convertView.findViewById(R.id.iv_image3);

            tv_name1 = (TextView) convertView.findViewById(R.id.tv_name1);
            tv_name2 = (TextView) convertView.findViewById(R.id.tv_name2);
            tv_name3 = (TextView) convertView.findViewById(R.id.tv_name3);

            ll_info2 = (LinearLayout) convertView.findViewById(R.id.ll_info2);
            ll_info3 = (LinearLayout) convertView.findViewById(R.id.ll_info3);
        }

        public static CategoryInfoHolder getHolder(View convertView) {
            CategoryInfoHolder titleHolder = (CategoryInfoHolder) convertView.getTag();
            if (titleHolder == null) {
                titleHolder = new CategoryInfoHolder(convertView);
                convertView.setTag(titleHolder);
            }
            return titleHolder;
        }
    }


    /**
     * title类型的item对应holder
     *
     * @author Administrator
     */
    static class CategoryTitleHolder {
        TextView tv_title;

        public CategoryTitleHolder(View convertView) {
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        }

        public static CategoryTitleHolder getHolder(View convertView) {
            CategoryTitleHolder titleHolder = (CategoryTitleHolder) convertView.getTag();
            if (titleHolder == null) {
                titleHolder = new CategoryTitleHolder(convertView);
                convertView.setTag(titleHolder);
            }
            return titleHolder;
        }
    }
}
