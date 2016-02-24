package com.exe.googleplay.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.AppInfo;
import com.exe.googleplay.bean.Home;
import com.exe.googleplay.http.HttpHelper;
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

    @Override
    protected View loadSuccessView() {
        View homeView = View.inflate(getActivity(), R.layout.ptr_listview, null);
        ptr_listview = (PullToRefreshListView) homeView.findViewById(R.id.ptr_listview);
        ptr_listview.setMode(PullToRefreshBase.Mode.BOTH);

        listview = ptr_listview.getRefreshableView();
//        listview.setAdapter(new HomeAdapter());


        return ptr_listview;
    }

    @Override
    protected Object loadData() {
//        final String url = "http://127.0.0.1:8090/home?index=0";
        final String url = "http://192.168.79.201:8080/homelist0";
        String result = HttpHelper.get(url);
//        Home home = parseHome(result);
//        Gson gson = new Gson();
//        Home home = gson.fromJson(result, Home.class);
        home = JsonUtil.parseJsonToBean(result, Home.class);

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