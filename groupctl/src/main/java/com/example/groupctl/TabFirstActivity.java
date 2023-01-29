package com.example.groupctl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TabFirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab_first);
        //处理从标签栏传来的参数
        String desc = String.format("我是%s页面，来着%s",
                "首页",getIntent().getExtras().getString("tag"));
        TextView tv_first = (TextView) findViewById(R.id.tv_first);
        tv_first.setText(desc);
    }
}