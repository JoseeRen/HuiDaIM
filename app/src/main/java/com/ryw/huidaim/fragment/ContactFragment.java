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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ryw.huidaim.R;
import com.ryw.huidaim.service.IMService;
import com.ryw.huidaim.util.ThreadUtils;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

import java.util.Collection;

/**
 * Created by Mr_Shadow on 2017/6/3.
 * 联系人的fragment
 */

public class ContactFragment extends Fragment {

    private ListView lv;

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
    }

    private void initData() {
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                //获取到服务里的连接 对象
                XMPPConnection conn = IMService.conn;
                //从服务器获取联系人数据
                //获取到花名册
                Roster roster = conn.getRoster();
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

    }
}
