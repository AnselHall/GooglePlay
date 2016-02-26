package com.exe.googleplay.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 *
 */
public class BaseAdapter<T> extends android.widget.BaseAdapter {
    private Context context;
    private ArrayList<T> list;

    public BaseAdapter(Context context, ArrayList<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
