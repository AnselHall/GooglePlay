package com.exe.googleplay.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.AppInfo;
import com.exe.googleplay.global.ImageLoaderCfg;
import com.exe.googleplay.http.Url;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 *
 */
public class AppAdapter extends BaseAdapter<AppInfo> {

    private Context context;
    private ArrayList<AppInfo> list;

    public AppAdapter(Context context, ArrayList<AppInfo> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppHolder holder;
//		if(convertView==null){
//			convertView = View.inflate(context, R.layout.adapter_home, null);
//			holder = new ViewHolder(convertView);
//			convertView.setTag(holder);
//		}else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_home, null);
        }
        holder = AppHolder.getHolder(convertView);

        AppInfo appInfo = list.get(position);

        holder.tv_name.setText(appInfo.getName());
        holder.tv_size.setText(Formatter.formatFileSize(context, appInfo.getSize()));
        holder.tv_des.setText(appInfo.getDes());
        holder.rb_stars.setRating(appInfo.getStars());

        //http://127.0.0.1:8090/image?name=  + appInfo.getIconUrl()
        ImageLoader.getInstance().displayImage(Url.HOST + appInfo.getIconUrl(), holder.iv_icon
                , ImageLoaderCfg.default_options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
//						LogUtil.e(this, "onLoadingStarted");
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
//						LogUtil.e(this, "onLoadingFailed");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//						LogUtil.e(this, "onLoadingComplete");
                        //对图片进行2次处理
//						RoundedBitmapDisplayer displayer = new RoundedBitmapDisplayer(10);
//						displayer.display(loadedImage, (ImageAware) view, LoadedFrom.MEMORY_CACHE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });

        return convertView;
    }

    static class AppHolder {
        ImageView iv_icon;
        TextView tv_name, tv_size, tv_des;
        RatingBar rb_stars;

        /**
         * 将初始化控件的操作封装到构造方法中
         *
         * @param convertView
         */
        public AppHolder(View convertView) {
            iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            rb_stars = (RatingBar) convertView.findViewById(R.id.rb_stars);
        }

        /**
         * 获取Holder
         *
         * @param convertView
         * @return
         */
        public static AppHolder getHolder(View convertView) {
            AppHolder viewHolder = (AppHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new AppHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
