package com.exe.googleplay.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.bmob.v3.Bmob;

/**
 * Created by user on 2016/2/18.
 */
public class GooglePlayApplication extends Application {
    private static GooglePlayApplication mContext;//项目中共有上下文对象
    private static Handler mainHandler;//主线程Handler

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        mainHandler = new Handler();

        initBmob();

        initImageLoader();
    }

    private void initBmob() {
        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "55e436a3761f8b7d4d314f95e129e392");
    }

    private void initImageLoader() {
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(mContext);
        config.threadPriority(Thread.NORM_PRIORITY - 2);//设置线程优先级
        config.denyCacheImageMultipleSizesInMemory();//拒绝保存多分不同size的image在内存中
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//设置硬盘缓存文件的名字如何生成
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 获取全局上下文对象
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取主线程Handler
     * @return
     */
    public static Handler getMainHandler() {
        return mainHandler;
    }
}
