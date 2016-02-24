package com.exe.googleplay.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.exe.googleplay.R;
import com.exe.googleplay.util.CommonUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 2016/2/23.
 */
public abstract class ContentPage extends FrameLayout {

    private LayoutParams params;

    /**
     * 标识Page状态的常量
     */
    public enum PageState {
        STATE_LOADING(0), STATE_EMPTY(1), STATE_ERROR(2), STATE_SUCCESS(3);

        private int value;

        PageState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private PageState mState = PageState.STATE_LOADING;
    private View loadingView;
    private View emptyView;
    private View errorView;
    private View successView;

    public ContentPage(Context context) {
        super(context);
        initView();
    }

    public ContentPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ContentPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (loadingView == null) {
            loadingView = View.inflate(getContext(), R.layout.page_loading, null);
        }
        addView(loadingView, params);

        if (emptyView == null) {
            emptyView = View.inflate(getContext(), R.layout.page_empty, null);
        }
        addView(emptyView, params);

        if (errorView == null) {
            errorView = View.inflate(getContext(), R.layout.page_error, null);
        }
        addView(errorView, params);

        if (successView == null) {
            successView = createSuccessView();
        }
        if (successView != null) {
            addView(successView, params);
        } else {
            throw new IllegalArgumentException("The Method createSuccessView() can not return null");
        }

        //根据不同状态显示对于View
        showPage();

        loadDataAndRefreshView();
    }

    private void loadDataAndRefreshView() {
        //使用简单的线程池执行请求任务，单线程池，防止划几个Fragment，就请求几次数据
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Object result = loadData();

                mState = checkResult(result);

                CommonUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        showPage();
                    }
                });
            }
        });
    }

    private PageState checkResult(Object result) {
        if (result == null) {
            //请求数据失败
            mState = PageState.STATE_ERROR;
        } else {
            if (result instanceof List) {
                //返回的数据为List集合，请求成功 判断是否有数据
                List data = (List) result;
                if (data.size() <= 0) {
                    mState = PageState.STATE_EMPTY;
                } else {
                    mState = PageState.STATE_SUCCESS;
                }
            } else {
                mState = PageState.STATE_SUCCESS;
            }
        }
        return mState;
    }


    private void showPage() {
        loadingView.setVisibility(mState == PageState.STATE_LOADING ? VISIBLE : GONE);
        emptyView.setVisibility(mState == PageState.STATE_EMPTY ? VISIBLE : GONE);
        errorView.setVisibility(mState == PageState.STATE_ERROR ? VISIBLE : GONE);
        successView.setVisibility(mState == PageState.STATE_SUCCESS ? VISIBLE : GONE);
    }

    /**
     * 由于每个界面的SuccessView都不同，应给由每个界面具体去实现
     *
     * @return
     */
    protected abstract View createSuccessView();

    /**
     * @return
     */
    protected abstract Object loadData();
}
