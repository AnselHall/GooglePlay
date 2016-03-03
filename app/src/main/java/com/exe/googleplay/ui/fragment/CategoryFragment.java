package com.exe.googleplay.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.exe.googleplay.R;
import com.exe.googleplay.bean.Category;
import com.exe.googleplay.bean.CategoryInfo;
import com.exe.googleplay.http.HttpHelper;
import com.exe.googleplay.http.Url;
import com.exe.googleplay.ui.adapter.CategoryAdapter;
import com.exe.googleplay.util.CommonUtil;
import com.exe.googleplay.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CategoryFragment extends BaseFragment {

    private ArrayList<Object> list = new ArrayList<>();
    private ListView listView;
    private CategoryAdapter adapter;

    @Override
    protected View loadSuccessView() {
        listView = (ListView) View.inflate(getActivity(), R.layout.listview, null);

        adapter = new CategoryAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        return listView;
    }

    @Override
    protected Object loadData() {
        String url = Url.CATEGORY;
        String result = HttpHelper.get(url);

        ArrayList<Category> categories = (ArrayList<Category>) JsonUtil.parseJsonToList(result, new TypeToken<List<Category>>() {}.getType());

        if(categories!=null && categories.size()>0){
            //遍历categories，获取当前的category对象，然后将其title和infos放入list
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                //将title放入list中
                list.add(category.getTitle());
                //将infos放入list中
                ArrayList<CategoryInfo> infos = category.getInfos();
//				for (int j = 0; j < infos.size(); j++) {
//					list.add(infos.get(i));
//				}
                list.addAll(infos);
            }
            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        return list;
    }
}
