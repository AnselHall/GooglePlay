package com.exe.googleplay.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.Subject;
import com.exe.googleplay.global.ImageLoaderCfg;
import com.exe.googleplay.http.Url;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 *
 */
public class SubjectAdapter extends BaseAdapter<Subject> {
    private Context context;
    private ArrayList<Subject> list;

    public SubjectAdapter(Context context, ArrayList<Subject> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_subject, null);
        }

        SubjectHolder holder = SubjectHolder.getHolder(convertView);

        Subject subject = list.get(position);

        holder.tv_des.setText(subject.getDes());
        ImageLoader.getInstance().displayImage(Url.HOST + subject.getUrl(), holder.iv_image,
                ImageLoaderCfg.default_options);

        return convertView;
    }

    static class SubjectHolder {
        ImageView iv_image;
        TextView tv_des;

        public SubjectHolder(View convertView) {
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            tv_des = (TextView) convertView.findViewById(R.id.tv_des);
        }

        public static SubjectHolder getHolder(View convertView) {
            SubjectHolder subjectHolder = (SubjectHolder) convertView.getTag();
            if (subjectHolder == null) {
                subjectHolder = new SubjectHolder(convertView);
                convertView.setTag(subjectHolder);
            }
            return subjectHolder;
        }
    }
}
