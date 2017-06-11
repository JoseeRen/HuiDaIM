/*
 * Create on 2017-6-11 上午8:29
 * FileName: ContactFragment.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-8 上午11:36
 * FileName: ContactFragment.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 下午2:41
 * FileName: ContactFragment.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ryw.huidaim.R;
import com.ryw.huidaim.activitys.ChatActivity;
import com.ryw.huidaim.provider.ContactOpenHelper;
import com.ryw.huidaim.provider.ContactProvider;
import com.ryw.huidaim.util.ThreadUtils;

import org.jivesoftware.smack.Roster;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mr_Shadow on 2017/6/3.
 * 联系人的fragment
 */

public class ContactFragment extends Fragment {

    private ListView lv;
    private ContactAdapter contactAdapter;
    private Roster roster;
    private ContentObserver observer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, null);
        lv = (ListView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化数据
        initData();
        initListener();
    }

    /**
     * listview条目 的点击事件
     */
    private void initListener() {
        //listview条目 的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//跳转聊天页面
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String account = cursor.getString(cursor.getColumnIndex(ContactOpenHelper.ContactTable.ACCOUNT));
                String nickname = cursor.getString(cursor.getColumnIndex(ContactOpenHelper.ContactTable.NICKNAME));
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("account", account);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        registerContentObserver();
        ThreadUtils.runInThread(new Runnable() {

            @Override
            public void run() {


                //填充listview  从数据库查询 出当前的数据
                final Cursor cursor = getContext().getContentResolver().query(Uri.parse(ContactProvider.CONTACT_URI), null, null, null, null);
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新ui  回主线程
                        contactAdapter = new ContactAdapter(getActivity(), cursor);
                        lv.setAdapter(contactAdapter);
                    }
                });


            }
        });
    }


    class ContactAdapter extends CursorAdapter {


        public ContactAdapter(Context context, Cursor c) {
            super(context, c);
        }

        public ContactAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        public ContactAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = View.inflate(context, R.layout.item_contact, null);
            ViewHolder holder = new ViewHolder(view);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();

            holder.account.setText(cursor.getString(cursor.getColumnIndex(ContactOpenHelper.ContactTable.ACCOUNT)));
            holder.nickname.setText(cursor.getString(cursor.getColumnIndex(ContactOpenHelper.ContactTable.NICKNAME)));

        }

        class ViewHolder {
            @InjectView(R.id.head)
            ImageView head;
            @InjectView(R.id.nickname)
            TextView nickname;
            @InjectView(R.id.account)
            TextView account;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }


    /**
     * 注册内容观察 者
     */
    private void registerContentObserver() {
        observer = new MyObserver(new Handler());
        //  为true时  对应子表的改变也会通知
        getContext().getContentResolver().registerContentObserver(ContactProvider.CONTACT_URI_URI, true, observer);
    }

    class MyObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //获取变化 后的数据 更新ui
            Log.e("有更新啊", "更新成功");
           /* Cursor cursor = getContext().getContentResolver().query(ContactProvider.CONTACT_URI_URI, null, null, null, null);
            lv.setAdapter(new ContactAdapter(getActivity(),cursor));*/
            if (contactAdapter != null) {
                contactAdapter.getCursor().requery();
            }
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterContentObserver();
    }

    /**
     * 解除内容观察者的注册
     */
    private void unRegisterContentObserver() {
        getContext().getContentResolver().unregisterContentObserver(observer);

    }

}
