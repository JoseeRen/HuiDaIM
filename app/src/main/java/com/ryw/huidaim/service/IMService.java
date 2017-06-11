/*
 * Create on 2017-6-11 上午8:29
 * FileName: IMService.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

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
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ryw.huidaim.provider.ContactOpenHelper;
import com.ryw.huidaim.provider.ContactProvider;
import com.ryw.huidaim.util.ThreadUtils;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;

/**
 * Created by Mr_Shadow on 2017/6/3.
 */

public class IMService extends Service {
    //生明一个连接对象
    public static XMPPConnection conn;
    public static String main_user;
    private Roster roster;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();

    }

    private void initData() {
        ThreadUtils.runInThread(new Runnable() {

            @Override
            public void run() {
                //获取到服务里的连接 对象
                XMPPConnection conn = IMService.conn;

                //从服务器获取联系人数据
                //获取到花名册
                roster = conn.getRoster();
                //监听服务器的联系人

                roster.addRosterListener(new MyRosterListener());
                Collection<RosterEntry> entries = roster.getEntries();
                for (RosterEntry entry : entries) {
                    String nickname = entry.getName();
                    String account = entry.getUser();
                    // Log.e("username>>>>",nickname+"...."+account);
                    saveAndUpdate(entry);

                }


            }
        });
    }


    /**
     * 保存或者更新到数据库里
     */
    private void saveAndUpdate(RosterEntry re) {
        //操作数据库
        //先更新  再插入
        ContentValues contentValues = new ContentValues();
        String name = re.getName();
        String account = re.getUser();


        if (TextUtils.isEmpty(name)) {
            //名字为空时   自己处理下    用acount中@符号前面 的
            name = account.substring(0, account.indexOf("@"));

        }
        contentValues.put(ContactOpenHelper.ContactTable.NICKNAME, name);
        contentValues.put(ContactOpenHelper.ContactTable.ACCOUNT, re.getUser());

        int update = getContentResolver().update(Uri.parse(ContactProvider.CONTACT_URI), contentValues, ContactOpenHelper.ContactTable.ACCOUNT + " =? ", new String[]{re.getUser()});
        if (update <= 0) {
            //执行插入操作
            getContentResolver().insert(Uri.parse(ContactProvider.CONTACT_URI), contentValues);
        }
    }

    private class MyRosterListener implements RosterListener {
        @Override
        public void entriesAdded(Collection<String> collection) {
            //返回参数 collection的值 是变化 后的联系人的在服务器中的地址值
            for (String s : collection) {
                RosterEntry entry = roster.getEntry(s);
                //更新数据库
                saveAndUpdate(entry);

            }
        }

        @Override
        public void entriesUpdated(Collection<String> collection) {
            for (String s : collection) {
                RosterEntry entry = roster.getEntry(s);
                //更新数据库
                saveAndUpdate(entry);

            }

        }

        @Override
        public void entriesDeleted(Collection<String> collection) {
            for (String s : collection) {
                getContentResolver().delete(Uri.parse(ContactProvider.CONTACT_URI), ContactOpenHelper.ContactTable.ACCOUNT + " =? ", new String[]{s});
            }
        }

        /**
         * 作用是： 当联系人在线状态改变时
         *
         * @param presence
         */
        @Override
        public void presenceChanged(Presence presence) {

        }
    }

}
