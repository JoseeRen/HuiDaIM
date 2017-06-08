/*
 * Create on 2017-6-8 上午11:36
 * FileName: ThreadUtils.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 下午2:32
 * FileName: ThreadUtils.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.util;

import android.os.Handler;

/**
 * Created by 不会飞的超人 on 2016/12/13.
 */

public class ThreadUtils {
    private static Handler handler = new Handler();

    //在子线程运行
    public static void runInThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    //在主线程运行
    public static void runInUIThread(Runnable runnable) {
        handler.post(runnable);
    }
}
