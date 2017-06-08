/*
 * Create on 2017-6-8 上午11:36
 * FileName: SessionFragment.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 下午2:42
 * FileName: SessionFragment.java
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

import com.ryw.huidaim.R;

/**
 * Created by Mr_Shadow on 2017/6/3.
 * 会话的fragment
 */

public class SessionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, null);

        return view;
    }
}
