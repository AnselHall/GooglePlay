package com.exe.googleplay.bean;

import java.util.ArrayList;

/**
 * Created by user on 2016/3/1.
 */
public class Category {
    private String title;
    private ArrayList<CategoryInfo> infos;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<CategoryInfo> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<CategoryInfo> infos) {
        this.infos = infos;
    }
}
