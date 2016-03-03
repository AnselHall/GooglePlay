package com.exe.googleplay.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.AppInfo;
import com.exe.googleplay.global.GooglePlayApplication;
import com.exe.googleplay.global.ImageLoaderCfg;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.manager.DownloadInfo;
import com.exe.googleplay.manager.DownloadManager;
import com.exe.googleplay.ui.view.ProgressArc;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 *
 */
public class HomeAdapter extends BasicAdapter<AppInfo> {
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
        holder = ViewHolder.getHolder(convertView);
        holder.initProgressBar();//由于是复用的item的view，所以每次都需要重置一下状态
        AppInfo appInfo = list.get(position);
        holder.setAppInfo(appInfo);

        holder.rb_stars.setRating(appInfo.getStars());
        holder.tv_des.setText(appInfo.getDes());
        holder.tv_name.setText(appInfo.getName());
        holder.tv_size.setText(Formatter.formatFileSize(context, appInfo.getSize()));
        holder.iv_icon.setImageResource(R.drawable.ic_pause);

        ImageLoader.getInstance().displayImage(Url.HOST + appInfo.getIconUrl(), holder.iv_icon, ImageLoaderCfg.default_options);

        return convertView;
    }

    static class ViewHolder implements DownloadManager.DownloadObserver, View.OnClickListener {

        ImageView iv_icon;
        TextView tv_name, tv_size, tv_des;
        RatingBar rb_stars;
        FrameLayout download_layout;
        TextView download_state;
        ProgressArc progressArc;

        AppInfo appInfo;

        public ViewHolder(View convertView) {
            iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            rb_stars = (RatingBar) convertView.findViewById(R.id.rb_stars);

            download_state = (TextView) convertView.findViewById(R.id.download_state);
            download_layout = (FrameLayout) convertView.findViewById(R.id.download_layout);

            progressArc = new ProgressArc(GooglePlayApplication.getContext());
            int arcDiameter = (int) CommonUtil.getDimens(R.dimen.progress_arc_width);
            progressArc.setArcDiameter(arcDiameter);//设置进度条半径
            progressArc.setProgressColor(Color.parseColor("#1F95FF"));//设置进度条的进度颜色
            initProgressBar();

            download_layout.addView(progressArc, new FrameLayout.LayoutParams(arcDiameter + 2, arcDiameter + 2));

            DownloadManager.getInstance().registerDownloadObserver(this);

            download_layout.setOnClickListener(this);
        }

        /**
         * 初始化进度条
         */
        private void initProgressBar() {
            progressArc.setForegroundResource(R.drawable.ic_download);//显示前景图片
            progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
            download_state.setText("下载");
        }

        public void setAppInfo(AppInfo appInfo) {
            this.appInfo = appInfo;

            DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
            if (downloadInfo != null) {
                refreshDownloadState(downloadInfo);//根据downloadInfo刷新一下UI
            }
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
            } else {
                convertView.setTag(holder);
            }
            return holder;
        }

        @Override
        public void onDownloadStateChange(final DownloadInfo downloadInfo) {
            if (downloadInfo.getId() != this.appInfo.getId()) return;
            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    refreshDownloadState(downloadInfo);
                }
            });
        }

        @Override
        public void onDownloadProgressChange(final DownloadInfo downloadInfo) {
            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (downloadInfo.getId() != appInfo.getId()) return;
                    float progress = downloadInfo.getCurrentLength() * 100f / downloadInfo.getSize();
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                    progressArc.setForegroundResource(R.drawable.ic_pause);
                    progressArc.setProgress(progress / 100, false);//0-1
                    LogUtil.e(this, "progress: " + progress);
                    download_state.setText((int) progress + "%");
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (v == download_layout) {
                DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
                if (downloadInfo == null) {
                    //说明从来没有下载过
                    DownloadManager.getInstance().download(appInfo);
                } else {
                    if (downloadInfo.getState() == DownloadManager.STATE_FINISH) {
                        //如果下载完成，则安装
                        DownloadManager.getInstance().installApk(downloadInfo);
                    } else if (downloadInfo.getState() == DownloadManager.STATE_DOWNLOADING
                            || downloadInfo.getState() == DownloadManager.STATE_WAITTING) {
                        //如果正在下载或者处于等待，则暂停
                        DownloadManager.getInstance().pause(appInfo);
                    } else if (downloadInfo.getState() == DownloadManager.STATE_PAUSE) {
                        DownloadManager.getInstance().download(appInfo);
                    } else {
                        //下载出错的情况，点击则应该重新下载
                        DownloadManager.getInstance().download(appInfo);
                    }
                }
            }
        }


        /**
         * 刷新下载状态
         *
         * @param downloadInfo
         */
        private void refreshDownloadState(DownloadInfo downloadInfo) {
            switch (downloadInfo.getState()) {
                case DownloadManager.STATE_PAUSE:
                    progressArc.setForegroundResource(R.drawable.ic_resume);
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    download_state.setText("暂停");
                    break;
                case DownloadManager.STATE_WAITTING:
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                    progressArc.setForegroundResource(R.drawable.ic_pause);
                    download_state.setText("等待");
                    break;
                case DownloadManager.STATE_FINISH:
                    progressArc.setForegroundResource(R.drawable.ic_install);
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    download_state.setText("安装");
                    break;
                case DownloadManager.STATE_ERROR:
                    progressArc.setForegroundResource(R.drawable.ic_redownload);
                    download_state.setText("重新下载");
                    break;
            }
        }
    }
}
