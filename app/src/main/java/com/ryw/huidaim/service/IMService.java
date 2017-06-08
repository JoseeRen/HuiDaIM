/*
 * Create on 2017-6-8 上午11:36
 * FileName: IMService.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 下午4:33
 * FileName: IMService.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by Mr_Shadow on 2017/6/3.
 */

public class IMService extends Service {
    //生明一个连接对象
    public static XMPPConnection conn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
