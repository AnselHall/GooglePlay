package com.exe.googleplay.http;

/**
 *
 */
public class Url {
    public static String HOST = "http://192.168.79.201:8080/googleplay/";

    public static String HOME = HOST + "app/homelist0";

    //http://127.0.0.1:8090/app?index=0
    public static String APP = HOST + "/app/applist1";

    public static String GAME = HOST + "/app/gamelist1";

    public static String SUBJECT = HOST + "/app/subjectlist1";

    public static String RECOMMEND = HOST + "/app/recommend";

    public static String CATEGORY = HOST + "app/category";

    public static String HOT = HOST + "app/hot";

    //http://192.168.79.201:8080/googleplay/app/com.youyuan.yyhl/com.youyuan.yyhl  详情页
    public static String DETAIL = HOST + "app/";

    public static final String DOWNLOAD = HOST + "app/";

    public static final String BREAK_DOWNLOAD = HOST + "";
}
