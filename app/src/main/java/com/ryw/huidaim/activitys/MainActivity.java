/*
 * Create on 2017-6-8 上午11:36
 * FileName: MainActivity.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

/*
 * Create on 2017-6-3 上午11:31
 * FileName: MainActivity.java
 * Author: Ren Yaowei
 * Blog: http://www.renyaowei.top
 * Email renyaowei@foxmail.com
 */

package com.ryw.huidaim.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryw.huidaim.R;
import com.ryw.huidaim.fragment.ContactFragment;
import com.ryw.huidaim.fragment.SessionFragment;
import com.ryw.huidaim.util.ToolbarUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity {

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.tabs)
    LinearLayout tabs;
    private ArrayList<Fragment> fragmentList;
    private String[] titles;
    private ToolbarUtil toolbarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initData();
        initListener();
    }

    private void initListener() {
        //viewpager的变化监听
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
        //设置询问页签的点击事件
        toolbarUtil.setOnToolbarClickListener(new MyOnToolbarClickListener());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //viewpager的数据的填充
        fragmentList = new ArrayList<>();
        fragmentList.add(new SessionFragment());
        fragmentList.add(new ContactFragment());


        viewpager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        //标题
        titles = new String[]{"会话", "联系人"};
        title.setText(titles[0]);

        int[] icons = {R.drawable.selector_meassage, R.drawable.selector_selfinfo};
        //底部页签
        toolbarUtil = new ToolbarUtil();
        toolbarUtil.createToolbar(tabs, titles, icons);
        //默认选中会话
        toolbarUtil.setTextColor(tabs, 0);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //更改标题
            title.setText(titles[position]);
            //改变选中的页签
            toolbarUtil.setTextColor(tabs, position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyOnToolbarClickListener implements ToolbarUtil.OnToolbarClickListener {
        @Override
        public void onToolbarClick(int position) {
            //切换viewpager
            viewpager.setCurrentItem(position);
        }
    }
}
