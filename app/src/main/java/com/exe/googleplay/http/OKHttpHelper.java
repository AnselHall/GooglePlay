package com.exe.googleplay.http;

import com.exe.googleplay.util.LogUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class OKHttpHelper {
    private static String TAG = "HttpHelper";

    public static void get(String url) {
        LogUtil.e(TAG, "url = " + url);
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        String result = null;
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtil.e(TAG, "onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtil.e(TAG, "onResponse");
                ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                int len = -1;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024 * 1024 * 10];
                while ((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }

                String result = new String(baos.toByteArray());
                LogUtil.e(TAG, "result = 1");
                LogUtil.e(TAG, "result = 2" + result);
                LogUtil.e(TAG, "result = 3");
                baos.close();
                inputStream.close();

            }
        });
    }
}
