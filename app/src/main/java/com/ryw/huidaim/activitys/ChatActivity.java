/*
 * Create on 2017-6-11 上午8:29
 * FileName: ChatActivity.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-10 下午2:12
 * FileName: ChatActivity.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.activitys;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ryw.huidaim.R;
import com.ryw.huidaim.provider.SmsOpenHelper;
import com.ryw.huidaim.provider.SmsProvider;
import com.ryw.huidaim.service.IMService;
import com.ryw.huidaim.util.ThreadUtils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr_Shadow on 2017/6/10.
 */

public class ChatActivity extends Activity {
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.lv_chat)
    ListView lvChat;
    @InjectView(R.id.et_content)
    EditText etContent;
    @InjectView(R.id.btn_send)
    Button btnSend;
    private Intent intent;
    private String account;
    private String nickname;
    private Message message;
    private Chat chat;
    private String from;
    private String to;
    private SmsAdapter adapter;
    private SmsObserver observer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        intent = getIntent();

        initData();
        registerObserver();
    }

    /**
     * 注册内容观察者
     */
    private void registerObserver() {
        observer = new SmsObserver(new Handler());
        getContentResolver().registerContentObserver(SmsProvider.URI_SMS, true, observer);
    }

    class SmsObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (adapter == null)
                return;
            adapter.getCursor().requery();
            lvChat.setSelection(lvChat.getCount() - 1);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
        }
    }

    private void initData() {
        account = intent.getStringExtra("account");
        nickname = intent.getStringExtra("nickname");
        title.setText("与" + nickname + "聊天中");

        ThreadUtils
                .runInThread(new Runnable() {
                    @Override
                    public void run() {
                        //查询 数据库的聊天信息，填充listview
                        final Cursor courso = getContentResolver().query(SmsProvider.URI_SMS, null, "(from_account = ? and to_account = ? ) or ( from_account = ? and to_account = ? )", new String[]{account, IMService.main_user, IMService.main_user, account}, SmsOpenHelper.SmsTable.TIME + " ASC ");
                        if (courso == null || courso.getCount() == 0) {
                            return;
                        }
                        ThreadUtils.runInUIThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new SmsAdapter(ChatActivity.this, courso);
                                lvChat.setAdapter(adapter);
                                lvChat.setSelection(lvChat.getCount() - 1);
                            }
                        });
                    }
                });


    }

    class SmsAdapter extends CursorAdapter {

        private View view;

        public SmsAdapter(Context context, Cursor c) {
            super(context, c);
        }

        public SmsAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        public SmsAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            //判断当前位置是发送还是接收
            String from_account = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.FROM_ACCOUNT));
            if (from_account.equals(account)) {
                //接受  左边
                view = View.inflate(ChatActivity.this, R.layout.item_chat_receive, null);
            } else {
                //发送 右边
                view = View.inflate(ChatActivity.this, R.layout.item_chat_send, null);
            }
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);


            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.content.setText(cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.BODY)));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Long time = Long.valueOf(cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.TIME)));
            holder.time.setText(simpleDateFormat.format(new Date(time)));

        }

        class ViewHolder {
            @InjectView(R.id.time)
            TextView time;

            @InjectView(R.id.content)
            TextView content;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }

    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
//获取输入框的聊天内容
        final String body = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(body)) {
            return;
        }
        XMPPConnection conn = IMService.conn;
        //下面现行代码最好 放到点击事件里
        ChatManager manager = conn.getChatManager();
        chat = manager.createChat(account, new MyMessageListener());

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {

                message = new Message();
                message.setTo(account);
                message.setFrom(IMService.main_user);
                message.setBody(body);
                message.setType(Message.Type.chat);
                try {
                    chat.sendMessage(message);
                    //保存本地数据库
                    //返回的是聊天对象
                    String participant = chat.getParticipant();
                    saveMessage(message, participant);
                } catch (XMPPException e) {
                    e.printStackTrace();
                } finally {
                    ThreadUtils.runInUIThread(new Runnable() {
                        @Override
                        public void run() {
                            //清空编辑框的内容
                            etContent.setText("");
                        }
                    });
                }
            }
        });


    }

    /**
     * 本地保存聊天记录
     *
     * @param message     聊天信息
     * @param participant 聊天对象
     */
    private void saveMessage(Message message, String participant) {
        ContentValues values = new ContentValues();
        from = message.getFrom();
        to = message.getTo();

        if (from.contains("/"))
            from = from.substring(0, from.indexOf("/"));
        if (to.contains("/"))
            to = to.substring(0, to.indexOf("/"));


        values.put(SmsOpenHelper.SmsTable.FROM_ACCOUNT, from);

        values.put(SmsOpenHelper.SmsTable.TO_ACCOUNT, to);
        values.put(SmsOpenHelper.SmsTable.BODY, message.getBody());
        values.put(SmsOpenHelper.SmsTable.TIME, System.currentTimeMillis());
        values.put(SmsOpenHelper.SmsTable.TYPE, message.getType().name());
        values.put(SmsOpenHelper.SmsTable.STATUS, "online");
        values.put(SmsOpenHelper.SmsTable.SESSION_ACCOUNT, participant);
        getContentResolver().insert(SmsProvider.URI_SMS, values);
    }

    /**
     * 监听 聊天
     */
    class MyMessageListener implements MessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {
            message.getType();
            String body = message.getBody();
            Log.e("rywwwww", body + "bodydddd");
            //保存聊天信息
            saveMessage(message, chat.getParticipant());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
    }
}
