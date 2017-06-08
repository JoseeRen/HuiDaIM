/*
 * Create on 2017-6-8 上午11:36
 * FileName: ToastUtil.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 不会飞的超人 on 2016/12/13.
 */

public class ToastUtil {
    public static void showToast(final Context context, final String text) {
        ThreadUtils.runInUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
