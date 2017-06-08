/*
 * Create on 2017-6-8 上午11:36
 * FileName: ContactOpenHelper.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 下午5:23
 * FileName: ContactOpenHelper.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Mr_Shadow on 2017/6/3.
 */

public class ContactOpenHelper extends SQLiteOpenHelper {
    public static final String T_CONTACT = "contact";

    //实现 了BaseColumns  如果和rpovider自动去识别_id这一列
    public class ContactTable implements BaseColumns {
        //昵称
        public static final String NICKNAME = "nickname";

        //用户名
        public static final String ACCOUNT = "account";
    }

    public ContactOpenHelper(Context context) {
        super(context, "contact.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String sql = "create table " + T_CONTACT + " (_id integer primary key autoincrement, " + ContactTable.ACCOUNT + " text , " + ContactTable.NICKNAME + " text );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
