package com.exe.googleplay.http;

import com.exe.googleplay.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
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

    /**
     * 下载文件，返回流对象
     *
     * @param url
     * @return
     */
    public static HttpResult download(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        boolean retry = true;
        while (retry) {
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if(httpResponse!=null){
                    return new HttpResult(httpClient, httpGet, httpResponse);
                }
            } catch (Exception e) {
                retry = false;
                e.printStackTrace();
                LogUtil.e(TAG, "download: "+e.getMessage());
            }
        }
        return null;
    }

    /**
     * Http返回结果的进一步封装
     * @author Administrator
     *
     */
    public static class HttpResult {
        private HttpClient httpClient;
        private HttpGet httpGet;
        private HttpResponse httpResponse;
        private InputStream inputStream;

        public HttpResult(HttpClient httpClient, HttpGet httpGet,
                          HttpResponse httpResponse) {
            super();
            this.httpClient = httpClient;
            this.httpGet = httpGet;
            this.httpResponse = httpResponse;
        }

        /**
         * 获取状态码
         * @return
         */
        public int getStatusCode() {
            StatusLine status = httpResponse.getStatusLine();
            return status.getStatusCode();
        }

        /**
         * 获取输入流
         * @return
         */
        public InputStream getInputStream(){
            if(inputStream==null && getStatusCode()<300){
                HttpEntity entity = httpResponse.getEntity();
                try {
                    inputStream =  entity.getContent();
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e(this, "getInputStream: "+e.getMessage());
                }
            }
            return inputStream;
        }

        /**
         * 关闭链接和流对象
         */
        public void close() {
            if (httpGet != null) {
                httpGet.abort();
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.e(this, "close: "+e.getMessage());
                }
            }
            //关闭链接
            if (httpClient != null) {
                httpClient.getConnectionManager().closeExpiredConnections();
            }
        }
    }
}
