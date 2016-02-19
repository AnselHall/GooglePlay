package com.exe.googleplay.util;

import android.widget.Toast;

import com.exe.googleplay.global.GooglePlayApplication;

/**
 * Created by user on 2016/2/18.
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 可以连续土司，不用等上一个吐司消失，直接显示
     * @param text
     */
    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(GooglePlayApplication.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }
}
