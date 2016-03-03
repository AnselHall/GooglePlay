package com.exe.googleplay.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.AppInfo;
import com.exe.googleplay.bean.SafeInfo;
import com.exe.googleplay.global.ImageLoaderCfg;
import com.exe.googleplay.http.HttpHelper;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.manager.DownloadInfo;
import com.exe.googleplay.manager.DownloadManager;
import com.exe.googleplay.ui.fragment.ContentPage;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.JsonUtil;
import com.exe.googleplay.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class AppDetailActivity extends ActionBarActivity implements View.OnClickListener, DownloadManager.DownloadObserver {

    private String packageName;//app 包名
    private ContentPage contentPage;
    private AppInfo appInfo;
    private ScrollView sv_container;

    //1.app info
    private ImageView iv_icon;
    private RatingBar rb_stars;
    private TextView tv_name, tv_download_num, tv_version, tv_date, tv_size;
    //2.app safe
    private int safeDesHeight;//app safe描述区域的高度
    private boolean isShowSafeDes = false;//是否显示描述区域
    private RelativeLayout rl_safe;
    private LinearLayout ll_safe_des;
    private ImageView iv_safe1, iv_safe2, iv_safe3, iv_safe_arrow;
    private ImageView iv_safe_des1, iv_safe_des2, iv_safe_des3;
    private TextView tv_safe_des1, tv_safe_des2, tv_safe_des3;
    //3.app screen
    private ImageView iv_screen1, iv_screen2, iv_screen3, iv_screen4, iv_screen5;
    //4.app des
    private int minDesHeight;//app 描述信息最小高度
    private int maxDesHeight;//app 描述信息最大高度
    private boolean isExpandDes = false;//是否展开整个app描述
    private LinearLayout ll_app_des;
    private TextView tv_des, tv_author;
    private ImageView iv_des_arrow;
    //5.app download
    private ProgressBar pb_progress;
    private Button btn_download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        packageName = getIntent().getStringExtra("packageName");
        contentPage = new ContentPage(this) {
            @Override
            protected Object loadData() {
                return AppDetailActivity.this.loadData();
            }

            @Override
            protected View createSuccessView() {
                View view = View.inflate(AppDetailActivity.this, R.layout.activity_app_detail, null);
                sv_container = (ScrollView) view.findViewById(R.id.sv_container);
                //1.app info
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                rb_stars = (RatingBar) view.findViewById(R.id.rb_stars);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_download_num = (TextView) view.findViewById(R.id.tv_download_num);
                tv_version = (TextView) view.findViewById(R.id.tv_version);
                tv_date = (TextView) view.findViewById(R.id.tv_date);
                tv_size = (TextView) view.findViewById(R.id.tv_size);
                //2.app safe
                rl_safe = (RelativeLayout) view.findViewById(R.id.rl_safe);
                ll_safe_des = (LinearLayout) view.findViewById(R.id.ll_safe_des);
                iv_safe1 = (ImageView) view.findViewById(R.id.iv_safe1);
                iv_safe2 = (ImageView) view.findViewById(R.id.iv_safe2);
                iv_safe3 = (ImageView) view.findViewById(R.id.iv_safe3);
                iv_safe_arrow = (ImageView) view.findViewById(R.id.iv_safe_arrow);
                iv_safe_des1 = (ImageView) view.findViewById(R.id.iv_safe_des1);
                iv_safe_des2 = (ImageView) view.findViewById(R.id.iv_safe_des2);
                iv_safe_des3 = (ImageView) view.findViewById(R.id.iv_safe_des3);
                tv_safe_des1 = (TextView) view.findViewById(R.id.tv_safe_des1);
                tv_safe_des2 = (TextView) view.findViewById(R.id.tv_safe_des2);
                tv_safe_des3 = (TextView) view.findViewById(R.id.tv_safe_des3);
                //3.app screen
                iv_screen1 = (ImageView) view.findViewById(R.id.iv_screen1);
                iv_screen2 = (ImageView) view.findViewById(R.id.iv_screen2);
                iv_screen3 = (ImageView) view.findViewById(R.id.iv_screen3);
                iv_screen4 = (ImageView) view.findViewById(R.id.iv_screen4);
                iv_screen5 = (ImageView) view.findViewById(R.id.iv_screen5);
                //4.app des
                ll_app_des = (LinearLayout) view.findViewById(R.id.ll_app_des);
                tv_des = (TextView) view.findViewById(R.id.tv_des);
                tv_author = (TextView) view.findViewById(R.id.tv_author);
                iv_des_arrow = (ImageView) view.findViewById(R.id.iv_des_arrow);
                //5.app download
                pb_progress = (ProgressBar) view.findViewById(R.id.pb_progress);
                btn_download = (Button) view.findViewById(R.id.btn_download);

                btn_download.setOnClickListener(AppDetailActivity.this);
                return view;
            }
        };
        setContentView(contentPage);//将activity的页面加载交给contentPage管理

        //注册下载监听器
        DownloadManager.getInstance().registerDownloadObserver(this);
    }

    /**
     * 加载数据
     *
     * @return
     */
    private Object loadData() {
        String url = Url.DETAIL + packageName + "/" + packageName;
        String response = HttpHelper.get(url);
        LogUtil.e(this, url);
        appInfo = JsonUtil.parseJsonToBean(response, AppInfo.class);

        if (appInfo != null) {
            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI();
                }
            });
        }
        return appInfo;
    }

    /**
     * 刷新各个模块的UI
     */
    private void refreshUI() {
        //1.显示app info模块
        showAppInfo();
        //2.显示app safe模块
        showAppSafe();
        //3.显示app screen模块
        showAppScreen();
        //4.显示app des模块
        showAppDes();

        //5.根据下载状态，让下载按钮显示不同文字
        DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
        if (downloadInfo != null) {
            refreshDownloadState(downloadInfo);
        }
    }

    private void showAppInfo() {
        ImageLoader.getInstance().displayImage(Url.HOST + appInfo.getIconUrl(), iv_icon, ImageLoaderCfg.default_options);
        tv_name.setText(appInfo.getName());
        rb_stars.setRating(appInfo.getStars());
        tv_download_num.setText("下载：" + appInfo.getDownloadNum());
        tv_version.setText("版本：" + appInfo.getVersion());
        tv_date.setText("日期：" + appInfo.getDate());
        tv_size.setText("大小：" + Formatter.formatFileSize(this, appInfo.getSize()));
    }

    private void showAppSafe() {
        //safe模块至少有一个bean，最多有3个
        ArrayList<SafeInfo> safe = appInfo.getSafe();
        SafeInfo safeInfo1 = safe.get(0);
        ImageLoader.getInstance().displayImage(Url.HOST + safeInfo1.getSafeUrl(), iv_safe1, ImageLoaderCfg.default_options);
        ImageLoader.getInstance().displayImage(Url.HOST + safeInfo1.getSafeDesUrl(), iv_safe_des1, ImageLoaderCfg.default_options);
        tv_safe_des1.setText(safeInfo1.getSafeDes());
        //由于第2和第3可能没有，所以要判断
        if (safe.size() > 1) {
            SafeInfo safeInfo2 = safe.get(1);
            ImageLoader.getInstance().displayImage(Url.HOST + safeInfo2.getSafeUrl(), iv_safe2, ImageLoaderCfg.default_options);
            ImageLoader.getInstance().displayImage(Url.HOST + safeInfo2.getSafeDesUrl(), iv_safe_des2, ImageLoaderCfg.default_options);
            tv_safe_des2.setText(safeInfo2.getSafeDes());
        } else {
            iv_safe_des2.setVisibility(View.GONE);
            tv_safe_des2.setVisibility(View.GONE);
        }
        if (safe.size() > 2) {
            SafeInfo safeInfo3 = safe.get(2);
            ImageLoader.getInstance().displayImage(Url.HOST + safeInfo3.getSafeUrl(), iv_safe3, ImageLoaderCfg.default_options);
            ImageLoader.getInstance().displayImage(Url.HOST + safeInfo3.getSafeDesUrl(), iv_safe_des3, ImageLoaderCfg.default_options);
            tv_safe_des3.setText(safeInfo3.getSafeDes());
        } else {
            iv_safe_des3.setVisibility(View.GONE);
            tv_safe_des3.setVisibility(View.GONE);
        }

        //1.获取ll_safe_des的高度
        ll_safe_des.measure(0, 0);//为了保证能获取到高度，所以先测量
        safeDesHeight = ll_safe_des.getMeasuredHeight();
        //2.隐藏ll_safe_des区域，将其高度设置为0
        ll_safe_des.getLayoutParams().height = 0;
        ll_safe_des.requestLayout();
        //3.设置点击事件
        rl_safe.setOnClickListener(this);
    }

    private void showAppScreen() {
        //由于截图一般是3-5张
        ArrayList<String> screen = appInfo.getScreen();
        ImageLoader.getInstance().displayImage(Url.HOST + screen.get(0), iv_screen1, ImageLoaderCfg.default_options);
        ImageLoader.getInstance().displayImage(Url.HOST + screen.get(1), iv_screen2, ImageLoaderCfg.default_options);
        ImageLoader.getInstance().displayImage(Url.HOST + screen.get(2), iv_screen3, ImageLoaderCfg.default_options);
        //显示4和5的时候要判断
        if (screen.size() > 3) {
            ImageLoader.getInstance().displayImage(Url.HOST + screen.get(3), iv_screen4, ImageLoaderCfg.default_options);
        } else {
            iv_screen4.setVisibility(View.GONE);
        }
        if (screen.size() > 4) {
            ImageLoader.getInstance().displayImage(Url.HOST + screen.get(4), iv_screen5, ImageLoaderCfg.default_options);
        } else {
            iv_screen5.setVisibility(View.GONE);
        }
    }

    private void showAppDes() {
        tv_des.setText(appInfo.getDes());
        tv_author.setText(appInfo.getAuthor());

        //由于需要让tv_des进行一个高度变化的动画，变化的范围：5行的高度-总高度
        //1.求出tv_des5行时候的高度
        tv_des.setMaxLines(5);
//		tv_des.measure(0, 0);//此时不起作用

        tv_des.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //一般用完之后，立即移除该监听
                tv_des.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                minDesHeight = tv_des.getMeasuredHeight();//获取5行时候的高度
//				LogUtil.e(this, "getHeight: "+tv_des.getHeight());
//				LogUtil.e(this, "measuredHeight: "+tv_des.getMeasuredHeight());

                //2.由于需要获取tv_des的总高度，所以再将它的maxLine还原
                tv_des.setMaxLines(Integer.MAX_VALUE);//会全部显示内容
                tv_des.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //一般用完之后，立即移除该监听
                        tv_des.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        maxDesHeight = tv_des.getMeasuredHeight();//获取总高度
//						LogUtil.e(this, "measuredHeight: "+tv_des.getMeasuredHeight());
                        //3.但是此时如果运行，会显示全部文本，但是我们需要只显示5行的高度
                        tv_des.getLayoutParams().height = minDesHeight;
                        tv_des.requestLayout();//让tv_des显示为5行的高度
                    }
                });
            }
        });
        //给整体描述区域布局设置点击事件
        ll_app_des.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_safe:
                ValueAnimator safeAnimator = null;
                if (isShowSafeDes) {
                    safeAnimator = ValueAnimator.ofInt(safeDesHeight, 0);
                } else {
                    safeAnimator = ValueAnimator.ofInt(0, safeDesHeight);
                }
                safeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int currentHeight = (Integer) animator.getAnimatedValue();
                        ll_safe_des.getLayoutParams().height = currentHeight;
                        ll_safe_des.requestLayout();
                    }
                });
                safeAnimator.setDuration(300);
                safeAnimator.addListener(new SafeAnimListener());
                safeAnimator.start();
                break;

            case R.id.ll_app_des:
                ValueAnimator desAnimator = null;
                if (isExpandDes) {
                    desAnimator = ValueAnimator.ofInt(maxDesHeight, minDesHeight);
                } else {
                    desAnimator = ValueAnimator.ofInt(minDesHeight, maxDesHeight);
                }
                desAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int currentHeight = (Integer) animator.getAnimatedValue();
                        tv_des.getLayoutParams().height = currentHeight;
                        tv_des.requestLayout();

                        //只有伸展动画的时候才需要内容向上滚动,收缩动画的时候是不需要滚动的
                        if (!isExpandDes) {
                            int scrollY = currentHeight - minDesHeight;
                            sv_container.scrollBy(0, scrollY);
                        }
                    }
                });
                desAnimator.setDuration(300);
                desAnimator.addListener(new DesAnimListener());
                desAnimator.start();
                break;
            case R.id.btn_download:
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
                break;
        }
    }

    @Override
    public void onDownloadStateChange(final DownloadInfo downloadInfo) {
        if (appInfo == null || appInfo.getId() != downloadInfo.getId()) return;
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                refreshDownloadState(downloadInfo);
            }
        });
    }

    @Override
    public void onDownloadProgressChange(final DownloadInfo downloadInfo) {
        if (appInfo == null || appInfo.getId() != downloadInfo.getId()) return;
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                float fraction = downloadInfo.getCurrentLength() * 100f / downloadInfo.getSize();
                pb_progress.setProgress((int) fraction);

                btn_download.setBackgroundResource(0);//移除下载按钮的背景
                btn_download.setText((int) fraction + "%");
            }
        });
    }

    /**
     * 安全模块动画的监听器
     *
     * @author Administrator
     */
    class SafeAnimListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationCancel(Animator arg0) {
        }

        @Override
        public void onAnimationEnd(Animator arg0) {
            isShowSafeDes = !isShowSafeDes;
            iv_safe_arrow.setBackgroundResource(isShowSafeDes ? R.drawable.arrow_up : R.drawable.arrow_down);
        }

        @Override
        public void onAnimationRepeat(Animator arg0) {
        }

        @Override
        public void onAnimationStart(Animator arg0) {
        }
    }

    /**
     * 描述区域动画的监听
     *
     * @author Administrator
     */
    class DesAnimListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationCancel(Animator arg0) {
        }

        @Override
        public void onAnimationEnd(Animator arg0) {
            isExpandDes = !isExpandDes;
            iv_des_arrow.setBackgroundResource(isExpandDes ? R.drawable.arrow_up : R.drawable.arrow_down);
        }

        @Override
        public void onAnimationRepeat(Animator arg0) {
        }

        @Override
        public void onAnimationStart(Animator arg0) {
        }

    }

    /**
     * 根据downloadInfo的state，显示不同的下载状态
     */
    private void refreshDownloadState(DownloadInfo downloadInfo) {
        btn_download.setBackgroundResource(R.drawable.selector_btn_download);
        switch (downloadInfo.getState()) {
            case DownloadManager.STATE_PAUSE:
                btn_download.setText("继续下载");
                break;
            case DownloadManager.STATE_FINISH:
                btn_download.setText("安装");
                break;
            case DownloadManager.STATE_ERROR:
                btn_download.setText("重新下载");
                break;
            case DownloadManager.STATE_WAITTING:
                btn_download.setText("等待中");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity销毁后，不需要在进行监听
        DownloadManager.getInstance().unregisterDownloadObserver(this);
    }
}
