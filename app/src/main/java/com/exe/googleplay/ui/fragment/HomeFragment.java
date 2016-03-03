package com.exe.googleplay.ui.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.AppInfo;
import com.exe.googleplay.bean.Home;
import com.exe.googleplay.http.HttpHelper;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.ui.activity.AppDetailActivity;
import com.exe.googleplay.ui.adapter.HomeAdapter;
import com.exe.googleplay.ui.adapter.HomeHeaderAdapter;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.JsonUtil;
import com.exe.googleplay.util.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 首页Fragment
 */
public class HomeFragment extends BaseFragment {
    private static String TAG = "HttpHelper";
    private Home home;
    private PullToRefreshListView ptr_listview;
    private ListView listview;
    private ArrayList<AppInfo> appInfoList = new ArrayList<>();
    private ArrayList<String> picList = new ArrayList<>();
    ;
    private HomeAdapter homeAdapter;
    private ViewPager header_view_pager;
    private HomeHeaderAdapter homeHeaderAdapter;

    @Override
    protected View loadSuccessView() {
        View homeView = View.inflate(getActivity(), R.layout.ptr_listview, null);
        ptr_listview = (PullToRefreshListView) homeView.findViewById(R.id.ptr_listview);
        ptr_listview.setMode(PullToRefreshBase.Mode.BOTH);

        listview = ptr_listview.getRefreshableView();
        View home_header = View.inflate(getContext(), R.layout.home_header, null);
        header_view_pager = (ViewPager) home_header.findViewById(R.id.view_pager);
        //动态设置VIewPager的高度
        calculateViewPagerHeight();
        listview.addHeaderView(home_header);

        homeAdapter = new HomeAdapter(getContext(), appInfoList);
        listview.setAdapter(homeAdapter);

        registerListener();

        return ptr_listview;
    }

    private void registerListener() {
        ptr_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    //上拉
                    contentPage.loadDataAndRefreshView();
                } else {
                    //下拉刷新
                    CommonUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            contentPage.loadDataAndRefreshView();
                            ptr_listview.onRefreshComplete();
                        }
                    });
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), AppDetailActivity.class);
                intent.putExtra("packageName", appInfoList.get(position - 2).getPackageName());
                LogUtil.e("packageName  :", appInfoList.get(position - 2).getPackageName());
                LogUtil.e("position    :", position + "");
                startActivity(intent);
            }
        });
    }

    /**
     * 根据图片的宽高比动态设置viewpager的高度
     */
    private void calculateViewPagerHeight() {
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();//获取ViewPager的宽
        //根据宽高比2.65来求出对应的高 width/height = 2.65
        float height = width / 2.65f;
        //给VIewPager重新设置高度
        header_view_pager.getLayoutParams().height = (int) height;
        header_view_pager.requestLayout();
    }

    @Override
    protected Object loadData() {
//        final String url = "http://127.0.0.1:8090/home?index=0";
        final String url = Url.HOME;
        String result = HttpHelper.get(url);
//        Home home = parseHome(result);
//        Gson gson = new Gson();

//        Home home = gson.fromJson(result, Home.class);
        home = JsonUtil.parseJsonToBean(result, Home.class);
        if (home != null) {

            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (home.getPicture() != null && home.getPicture().size() > 0) {
                        picList.addAll(home.getPicture());
                        homeHeaderAdapter = new HomeHeaderAdapter(picList);
                        header_view_pager.setAdapter(homeHeaderAdapter);
                    }

                    appInfoList.addAll(home.getList());
                    homeAdapter.notifyDataSetChanged();
                    ptr_listview.onRefreshComplete();
                }
            });
        }
        return home;
    }

    private Home parseHome(String result) {

        Home home = null;
        if (!TextUtils.isEmpty(result)) {
            home = new Home();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                JSONArray picture = jsonObject.getJSONArray("picture");
                ArrayList<String> picArray = new ArrayList<>();
                for (int i = 0; i < picture.length(); i++) {
                    String string = picture.getString(i);
                    picArray.add(string);
                }
                home.setPicture(picArray);

                JSONArray list = jsonObject.getJSONArray("list");
                ArrayList<AppInfo> listArray = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    AppInfo appInfo = new AppInfo();
                    JSONObject obj = list.getJSONObject(i);
                    appInfo.setDes(obj.getString("des"));
                    appInfo.setDownloadUrl(obj.getString("downloadUrl"));
                    appInfo.setIconUrl(obj.getString("iconUrl"));
                    appInfo.setId(obj.getInt("id"));
                    appInfo.setName(obj.getString("name"));
                    appInfo.setPackageName(obj.getString("packageName"));
                    appInfo.setSize(obj.getLong("size"));
                    appInfo.setStars((float) obj.getDouble("stars"));

                    listArray.add(appInfo);
                }
                home.setList(listArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return home;
    }
}