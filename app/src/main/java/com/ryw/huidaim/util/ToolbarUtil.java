/*
 * Create on 2017-6-11 上午8:29
 * FileName: ToolbarUtil.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-8 上午11:36
 * FileName: ToolbarUtil.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 下午3:25
 * FileName: ToolbarUtil.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.util;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryw.huidaim.R;

/**
 * Created by Mr_Shadow on 2017/6/3.
 * 底部生成页签工具类
 */

public class ToolbarUtil {
    private OnToolbarClickListener mListener;

    public void createToolbar(LinearLayout ll, String[] titles, int[] icons) {

        for (int i = 0; i < titles.length; i++) {
            final int position = i;
            TextView tv = (TextView) View.inflate(ll.getContext(), R.layout.inflate_toolbar_btn, null);
            tv.setText(titles[i]);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, icons[i], 0, 0);
            //设置点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onToolbarClick(position);
                    }
                }
            });
            int width = 0;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.weight = 1;
            ll.addView(tv, params);


        }

    }

    /**
     * 改变页签的颜色
     */
    public void setTextColor(LinearLayout ll, int position) {
        for (int i = 0; i < ll.getChildCount(); i++) {

            TextView tv = (TextView) ll.getChildAt(i);
            if (i == position) {
                tv.setSelected(true);
            } else {
                tv.setSelected(false);
            }
        }

    }

    public interface OnToolbarClickListener {
        void onToolbarClick(int position);


    }

    public void setOnToolbarClickListener(OnToolbarClickListener onToolbarClickListener) {
        this.mListener = onToolbarClickListener;


    }
}
