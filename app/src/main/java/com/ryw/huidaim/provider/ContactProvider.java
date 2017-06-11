/*
 * Create on 2017-6-11 上午8:29
 * FileName: ContactProvider.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-8 上午11:36
 * FileName: ContactProvider.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 下午4:53
 * FileName: ContactProvider.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Mr_Shadow on 2017/6/3.
 */

public class ContactProvider extends ContentProvider {
    //  获取全类名
    public static final String AUTHORTIES = ContactProvider.class.getCanonicalName();
    //uri匹配器
    public static UriMatcher mUriMathcer;


    //uri: "content://主机名/path"

    public static String CONTACT_URI = "content://" + AUTHORTIES + "/contact";
    public static Uri CONTACT_URI_URI = Uri.parse(CONTACT_URI);

    private static final int CONTACT = 0;

    //初始化匹配器对象
    static {
        mUriMathcer = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMathcer.addURI(AUTHORTIES, "/contact", CONTACT);
    }

    private ContactOpenHelper mHelper;
    private int update;
    private int delete;
    private Cursor query;

    @Override
    public boolean onCreate() {
        //初始化openhelper
        if (mHelper == null) {
            mHelper = new ContactOpenHelper(getContext());

        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int code = mUriMathcer.match(uri);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        switch (code) {
            case CONTACT:
                //联系人的表

                query = db.query(ContactOpenHelper.T_CONTACT, projection, selection, selectionArgs, null, null, sortOrder);

                break;
        }
        if (query != null) {
            Log.e("rywwwwww", "query success=========");
        }
        return query;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int code = mUriMathcer.match(uri);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = 0;
        switch (code) {
            case CONTACT:
                //联系人的表
                id = db.insert(ContactOpenHelper.T_CONTACT, "", values);


                break;
        }
        if (id != -1) {
            //插入成功
            Log.e("rywwwwwwwww", "insaert success=====================");
            //TODO 通知内容观察者  更新了
            //如果第二个参数 为空  则是通知所有
            getContext().getContentResolver().notifyChange(Uri.parse(CONTACT_URI), null);
            uri = ContentUris.withAppendedId(uri, id);


        }

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        int code = mUriMathcer.match(uri);
        switch (code) {
            case CONTACT:
                //联系人的表
                delete = db.delete(ContactOpenHelper.T_CONTACT, selection, selectionArgs);

                break;
        }
        if (delete > 0) {
            //删除成功
            Log.e("rywwwwwwwww", "delete success=====================");
            //TODO 通知内容观察者  更新了
            //如果第二个参数 为空  则是通知所有
            getContext().getContentResolver().notifyChange(Uri.parse(CONTACT_URI), null);
        }
        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        int code = mUriMathcer.match(uri);
        switch (code) {
            case CONTACT:
                //联系人的表
                update = db.update(ContactOpenHelper.T_CONTACT, values, selection, selectionArgs);


                break;
        }
        if (update > 0) {
            Log.e("rywwwwwwwww", "update  success=================");
            //TODO 通知内容观察者  更新了
            //如果第二个参数 为空  则是通知所有
            getContext().getContentResolver().notifyChange(Uri.parse(CONTACT_URI), null);
        }
        return update;
    }
}
