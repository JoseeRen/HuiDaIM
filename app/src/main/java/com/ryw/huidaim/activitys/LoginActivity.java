/*
 * Create on 2017-6-8 上午11:36
 * FileName: LoginActivity.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 上午11:47
 * FileName: LoginActivity.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ryw.huidaim.R;
import com.ryw.huidaim.service.IMService;
import com.ryw.huidaim.util.ThreadUtils;
import com.ryw.huidaim.util.ToastUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mr_Shadow on 2017/6/3.
 */

public class LoginActivity extends Activity {
    @InjectView(R.id.et_name)
    EditText mEtName;
    @InjectView(R.id.et_password)
    EditText mEtPassword;
    @InjectView(R.id.btn_login)
    Button mBtnLogin;
    private String username;
    private String password;
    private static final String HOST = "192.168.1.182";
    private static final int PORT = 5222;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initListener();
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //获取用户名和密码
                username = mEtName.getText().toString().trim();
                password = mEtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    mEtName.setError("用户名不能为空");
                    return;

                }
                if (TextUtils.isEmpty(password)) {
                    mEtPassword.setError("密码不能为空");
                    return;
                }
                //登录
                login();
            }
        });
    }

    /**
     * 登录
     */
    private void login() {
        new ThreadUtils().runInThread(new Runnable() {
            @Override
            public void run() {
                //创建配置信息对象    第一个参数是ip    第二个参数是端口号
                ConnectionConfiguration configuration = new ConnectionConfiguration(HOST, PORT);
                configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                configuration.setDebuggerEnabled(true);
                //创建连接对象
                XMPPConnection conn = new XMPPConnection(configuration);
                //保存连接 对象到服务中
                IMService.conn = conn;
                try {
                    //连接服务器
                    conn.connect();
                    //登录
                    conn.login(username, password);
                    ToastUtil.showToast(LoginActivity.this, "登录成功");

//跳转到主页面
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (XMPPException e) {
                    e.printStackTrace();
                    ToastUtil.showToast(LoginActivity.this, "登录失败");
                }
            }
        });

    }


}
