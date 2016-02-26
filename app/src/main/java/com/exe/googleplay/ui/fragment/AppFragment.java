package com.exe.googleplay.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.AppInfo;
import com.exe.googleplay.http.HttpHelper;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.ui.adapter.AppAdapter;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/2/22.
 */
public class AppFragment extends BaseFragment{
    private PullToRefreshListView refreshListView;
    private View view;
    private ListView listView;

    private AppAdapter adapter;
    private ArrayList<AppInfo> list = new ArrayList<AppInfo>();

    @Override
    protected View loadSuccessView() {
        view = View.inflate(getActivity(), R.layout.ptr_listview, null);
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.ptr_listview);
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView = refreshListView.getRefreshableView();
        listView.setDividerHeight(0);

        adapter = new AppAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if(refreshListView.getCurrentMode()== PullToRefreshBase.Mode.PULL_FROM_END){
                    //上拉
                    contentPage.loadDataAndRefreshView();
                }else {
                    //下拉
                    CommonUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            //完成刷新
                            refreshListView.onRefreshComplete();
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    protected Object loadData() {
        String url = Url.APP;

        String result = HttpHelper.get(url);

        ArrayList<AppInfo> appInfos = (ArrayList<AppInfo>) JsonUtil.parseJsonToList(result, new TypeToken<List<AppInfo>>(){}.getType());

        if(appInfos!=null && appInfos.size()>0){
            //将获取到的数据加到list中，然后更新UI
            list.addAll(appInfos);

            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();

                    //完成刷新
                    refreshListView.onRefreshComplete();
                }
            });
        }

        return result;
    }
}
