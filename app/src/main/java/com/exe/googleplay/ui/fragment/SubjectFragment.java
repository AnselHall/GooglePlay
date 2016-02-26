package com.exe.googleplay.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.Subject;
import com.exe.googleplay.http.HttpHelper;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.ui.adapter.SubjectAdapter;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class SubjectFragment extends BaseFragment{
    private PullToRefreshListView refreshListView;
    private View view;
    private ListView listView;
    private ArrayList<Subject> list = new ArrayList<Subject>();

    private SubjectAdapter adapter;
    @Override
    protected View loadSuccessView() {
        view = View.inflate(getActivity(), R.layout.ptr_listview, null);
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.ptr_listview);
        refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView = refreshListView.getRefreshableView();
        listView.setDividerHeight(0);

        adapter = new SubjectAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if(refreshListView.getCurrentMode()== PullToRefreshBase.Mode.PULL_FROM_END){
                    contentPage.loadDataAndRefreshView();
                }else {
                    //下拉
                    CommonUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
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

        String url = Url.SUBJECT;
        String result = HttpHelper.get(url);

        ArrayList<Subject> subjects = (ArrayList<Subject>) JsonUtil.parseJsonToList(result, new TypeToken<List<Subject>>(){}.getType());
        if(subjects!=null && subjects.size()>0){
            list.addAll(subjects);

            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    refreshListView.onRefreshComplete();
                }
            });
        }
        return result;
    }
}