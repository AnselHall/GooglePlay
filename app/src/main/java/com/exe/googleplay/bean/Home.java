package com.exe.googleplay.bean;

import java.util.ArrayList;

/**
 * Created by user on 2016/2/24.
 */
public class Home {
    private ArrayList<String> picture;

    private ArrayList<AppInfo> list;

    public ArrayList<String> getPicture() {
        return picture;
    }

    public void setPicture(ArrayList<String> picture) {
        this.picture = picture;
    }

    public ArrayList<AppInfo> getList() {
        return list;
    }

    public void setList(ArrayList<AppInfo> list) {
        this.list = list;
    }
}
