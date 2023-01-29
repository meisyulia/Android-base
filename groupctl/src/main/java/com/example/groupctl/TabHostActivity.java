package com.example.groupctl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class TabHostActivity extends TabActivity implements View.OnClickListener {

    private static final String TAG = "TabHostActivity";
    private static final String FIRST_TAG = "first";
    private static final String SECOND_TAG = "second";
    private static final String THIRD_TAG = "third";
    private LinearLayout ll_first;
    private LinearLayout ll_second;
    private LinearLayout ll_third;
    private Bundle mBundle = new Bundle();
    private TabHost tab_host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host);
        mBundle.putString("tag",TAG); //往包裹中存入名为tag的标记串
        ll_first = (LinearLayout) findViewById(R.id.ll_first);
        ll_second = (LinearLayout) findViewById(R.id.ll_second);
        ll_third = (LinearLayout) findViewById(R.id.ll_third);
        ll_first.setOnClickListener(this);
        ll_second.setOnClickListener(this);
        ll_third.setOnClickListener(this);
        //获取系统自动的标签栏，其实就是id为“@android:id/tabhost”的控件
        tab_host = getTabHost();
        //往标签栏添加标签，其中内容视频展示TabActivity
        tab_host.addTab(getNewTab(FIRST_TAG,R.string.menu_first,R.drawable.tab_first_selector,TabFirstActivity.class));
        tab_host.addTab(getNewTab(SECOND_TAG,R.string.menu_second,R.drawable.tab_second_selector,TabSecondActivity.class));
        tab_host.addTab(getNewTab(THIRD_TAG,R.string.menu_third,R.drawable.tab_third_selector,TabThirdActivity.class));
        //默认选第一个当视图
        changeContainerView(ll_first);
    }

    private TabHost.TabSpec getNewTab(String spec, int label, int icon, Class<?> cls) {
        Intent intent = new Intent(this, cls).putExtras(mBundle);
        //生成并返回新的标签规格（包括内容意图，标签文字和标签图标
        return tab_host.newTabSpec(spec).setContent(intent).setIndicator(getString(label),getResources().getDrawable(icon));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_first || v.getId() == R.id.ll_second || v.getId() == R.id.ll_third){
            //展示对应标签的内容视图
            changeContainerView(v);
        }
    }

    private void changeContainerView(View v) {
        ll_first.setSelected(false);
        ll_second.setSelected(false);
        ll_third.setSelected(false);
        v.setSelected(true);
        if (v == ll_first){
            tab_host.setCurrentTabByTag(FIRST_TAG);
        }else if(v == ll_second){
            tab_host.setCurrentTabByTag(SECOND_TAG);
        }else if(v == ll_third){
            tab_host.setCurrentTabByTag(THIRD_TAG);
        }
    }
}