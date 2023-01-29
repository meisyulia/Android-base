package com.example.groupctl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.groupctl.fragment.TabFirstFragment;
import com.example.groupctl.fragment.TabSecondFragment;
import com.example.groupctl.fragment.TabThirdFragment;

public class TabFragmentActivity extends AppCompatActivity {

    private static final String TAG = "TabFragmentActivity";
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_fragment);
        Bundle bundle = new Bundle();
        bundle.putString("tag",TAG);
        //获取碎片标题栏
        tabHost = findViewById(android.R.id.tabhost);
        //把设计的内容框架安装到碎片标题栏上
        tabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        //往标签栏添加标签，指定里面的内容视图
        tabHost.addTab(getTabView(R.string.menu_first,R.drawable.tab_first_selector), TabFirstFragment.class,bundle);
        tabHost.addTab(getTabView(R.string.menu_second,R.drawable.tab_second_selector), TabSecondFragment.class,bundle);
        tabHost.addTab(getTabView(R.string.menu_third,R.drawable.tab_third_selector), TabThirdFragment.class,bundle);
        //不显示各标签之间的分隔线
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    //根据字符串和图标资源编号，获得对应的标签规格
    private TabHost.TabSpec getTabView(int textId, int imgId) {
        String text = getResources().getString(textId);
        Drawable drawable = getResources().getDrawable(imgId);
        //设置图形的四周边界，必须设置图标大小，否则无法显示图标
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        View item_tabbar = getLayoutInflater().inflate(R.layout.item_tabbar, null);
        TextView tv_item = item_tabbar.findViewById(R.id.tv_item_tabbar);
        tv_item.setText(text);
        //文字上方显示图标
        tv_item.setCompoundDrawables(null,drawable,null,null);
        //生成并返回该标签对应的标签规格
        return tabHost.newTabSpec(text).setIndicator(item_tabbar);
    }
}