package com.exe.googleplay.ui.adapter;

import android.content.Context;
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

import java.util.ArrayList;

/**
 *
 */
public class HomeAdapter extends BaseAdapter<AppInfo> {
    private Context context;
    private ArrayList<AppInfo> list;

    public HomeAdapter(Context context, ArrayList<AppInfo> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_home, null);
        }
        holder =  ViewHolder.getHolder(convertView);
        AppInfo appInfo = list.get(position);

        holder.rb_stars.setRating(appInfo.getStars());
        holder.tv_des.setText(appInfo.getDes());
        holder.tv_name.setText(appInfo.getName());
        holder.tv_size.setText(Formatter.formatFileSize(context,appInfo.getSize()));
        holder.iv_icon.setImageResource(R.drawable.ic_pause);

        ImageLoader.getInstance().displayImage(Url.HOST + appInfo.getIconUrl(),holder.iv_icon,ImageLoaderCfg.default_options);

        return convertView;
    }

    static class ViewHolder {

        ImageView iv_icon;
        TextView tv_name, tv_size, tv_des;
        RatingBar rb_stars;

        public ViewHolder(View convertView) {
            iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            rb_stars = (RatingBar) convertView.findViewById(R.id.rb_stars);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder  holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
            } else {
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
