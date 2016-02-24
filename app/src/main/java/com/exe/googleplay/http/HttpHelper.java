package com.exe.googleplay.http;

import com.exe.googleplay.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 *
 */
public class HttpHelper {
    private static String TAG = "HttpHelper";

    public static String get(String url) {
        LogUtil.e(TAG, "url = " + url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        String result = "";
        try {
            HttpResponse execute = httpClient.execute(httpGet);
            if (execute.getStatusLine().getStatusCode() < 300) {
                HttpEntity entity = execute.getEntity();
                InputStream content = entity.getContent();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024 * 4];
                int len = -1;
                while ((len = content.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }

                result = new String(baos.toByteArray());
                LogUtil.e(TAG, "result = " + result);
                content.close();
                baos.close();
//                httpClient.getConnectionManager().closeExpiredConnections();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
