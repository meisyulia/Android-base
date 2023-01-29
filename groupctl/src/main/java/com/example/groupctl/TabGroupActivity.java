package com.example.groupctl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class TabGroupActivity extends ActivityGroup implements View.OnClickListener {
    private static final String TAG = "TabGroupActivity";
    private Bundle mBundle = new Bundle(); // 声明一个包裹对象
    private LinearLayout ll_container, ll_first, ll_second, ll_third;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_group);
        // 从布局文件中获取名叫ll_container的线性布局，用于存放内容视图
        ll_container = findViewById(R.id.ll_container);
        ll_first = findViewById(R.id.ll_first); // 获取第一个标签的线性布局
        ll_second = findViewById(R.id.ll_second); // 获取第二个标签的线性布局
        ll_third = findViewById(R.id.ll_third); // 获取第三个标签的线性布局
        ll_first.setOnClickListener(this); // 给第一个标签注册点击监听器
        ll_second.setOnClickListener(this); // 给第二个标签注册点击监听器
        ll_third.setOnClickListener(this); // 给第三个标签注册点击监听器
        mBundle.putString("tag", TAG); // 往包裹中存入名叫tag的标记串
        changeContainerView(ll_first); // 默认显示第一个标签的内容视图
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_first || v.getId() == R.id.ll_second || v.getId() == R.id.ll_third) {
            changeContainerView(v); // 点击了哪个标签，就切换到该标签对应的内容视图
        }
    }

    private void changeContainerView(View v) {
        ll_first.setSelected(false);
        ll_second.setSelected(false);
        ll_third.setSelected(false);
        v.setSelected(true);
        if (v == ll_first){
            toActivity("first",TabFirstActivity.class);
        }else if (v == ll_second){
            toActivity("second",TabSecondActivity.class);
        }else if (v == ll_third){
            toActivity("third",TabThirdActivity.class);
        }
    }

    private void toActivity(String label, Class<?> cls) {
        //创建意图
        Intent intent = new Intent(this, cls).putExtras(mBundle);
        ll_container.removeAllViews();
        //启动意图指向的活动，并获取该活动页面的顶层视图
        View v = getLocalActivityManager().startActivity(label, intent).getDecorView();
        //设置内容视图的布局参数
        v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT));
        //把活动页面的顶层视图（即内容视图）添加到内容框架上
        ll_container.addView(v);
    }
}