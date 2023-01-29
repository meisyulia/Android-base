package com.example.customctl;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customctl.util.MeasureUtil;

public class MeasureLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_layout);
        LinearLayout ll_header = (LinearLayout) findViewById(R.id.ll_header);
        TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
        float realHeight = MeasureUtil.getRealHeight(ll_header);
        String desc = String.format("上面下拉刷新头部的高度是%f", realHeight);
        tv_desc.setText(desc);
    }
}