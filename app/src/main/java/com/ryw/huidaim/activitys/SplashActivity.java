/*
 * Create on 2017-6-8 上午11:36
 * FileName: SplashActivity.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 上午11:32
 * FileName: SplashActivity.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.ryw.huidaim.R;
import com.ryw.huidaim.util.ThreadUtils;

/**
 * Created by Mr_Shadow on 2017/6/3.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                //停留2秒后跳转到登录页面
                SystemClock.sleep(2000);
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}
