package com.exe.googleplay.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.AppInfo;
import com.exe.googleplay.bean.Home;
import com.exe.googleplay.http.HttpHelper;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.ui.adapter.HomeAdapter;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.JsonUtil;
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
    private ArrayList<AppInfo> appInfoList;
    private HomeAdapter homeAdapter;

    @Override
    protected View loadSuccessView() {
        View homeView = View.inflate(getActivity(), R.layout.ptr_listview, null);
        ptr_listview = (PullToRefreshListView) homeView.findViewById(R.id.ptr_listview);
        ptr_listview.setMode(PullToRefreshBase.Mode.BOTH);

        listview = ptr_listview.getRefreshableView();
        appInfoList = new ArrayList<>();
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

                }
            }
        });
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

        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                appInfoList.addAll(home.getList());
                homeAdapter.notifyDataSetChanged();
            }
        });
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