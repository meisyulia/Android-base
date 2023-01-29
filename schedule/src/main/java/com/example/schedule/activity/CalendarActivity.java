package com.example.schedule.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.schedule.R;
import com.example.schedule.adapter.CalendarPageAdapter;
import com.example.schedule.util.DateUtil;


public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        PagerTabStrip pts_calendar = (PagerTabStrip) findViewById(R.id.pts_calendar);
        pts_calendar.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        pts_calendar.setTextColor(Color.BLACK);
        TextView tv_calendar = (TextView) findViewById(R.id.tv_calendar);
        tv_calendar.setText(DateUtil.getNowYearCN()+"日历");

        ViewPager vp_calendar = (ViewPager) findViewById(R.id.vp_calendar);
        CalendarPageAdapter adapter = new CalendarPageAdapter(getSupportFragmentManager());
        vp_calendar.setAdapter(adapter);
        vp_calendar.setCurrentItem(DateUtil.getNowMonth()-1);
    }
}