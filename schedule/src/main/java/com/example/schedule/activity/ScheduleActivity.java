package com.example.schedule.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.schedule.R;
import com.example.schedule.adapter.SchedulePagerAdapter;
import com.example.schedule.calendar.SpecialCalendar;
import com.example.schedule.util.DateUtil;

public class ScheduleActivity extends FragmentActivity {

    public static final String ACTION_FRAGMENT_SELECTED = "com.example.schedule.activity.ACTION_FRAGMENT_SELECTED";
    public static final String EXTRA_SELECTED_WEEK = "selected_week";
    public static final String ACTION_SHOW_FESTIVAL = "com.example.schedule.activity.ACTION_SHOW_FESTIVAL";
    public static final String EXTRA_FESTIVAL_RES = "festival_res";
    private static final String TAG = "ScheduleActivity";
    private LinearLayout ll_schedule;
    private ViewPager vp_schedule;
    private int m_week;
    private FestivalReceiver festivalReceiver;
    private int m_resid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        PagerTabStrip pts_schedule = (PagerTabStrip) findViewById(R.id.pts_schedule);
        pts_schedule.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        pts_schedule.setTextColor(Color.BLACK);
        ll_schedule = (LinearLayout) findViewById(R.id.ll_schedule);
        vp_schedule = (ViewPager) findViewById(R.id.vp_schedule);
        TextView tv_schedule = (TextView) findViewById(R.id.tv_schedule);
        tv_schedule.setText(DateUtil.getNowYearCN()+" 日程安排");

        SchedulePagerAdapter adapter = new SchedulePagerAdapter(getSupportFragmentManager());
        vp_schedule.setAdapter(adapter);
        m_week = SpecialCalendar.getTodayWeek();
        vp_schedule.setCurrentItem(m_week-1);
        vp_schedule.addOnPageChangeListener(new ScheduleChangeListener());
        mHandler.postDelayed(mFirst,50);
    }

    private final Handler mHandler = new Handler();
    private Runnable mFirst = new Runnable() {
        @Override
        public void run() {
            sendBroadcast(m_week);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        festivalReceiver = new FestivalReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(festivalReceiver,new IntentFilter(ACTION_SHOW_FESTIVAL));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(festivalReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //当横屏禹竖屏之间翻转时，不会重新onCreate,只会onResume
        if (m_resid != 0){
            ll_schedule.setBackgroundResource(m_resid);
        }
    }

    private class ScheduleChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected: position="+position+",m_week="+m_week);
            m_week = position+1;
            sendBroadcast(m_week);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void sendBroadcast(int m_week) {
        Intent intent = new Intent(ACTION_FRAGMENT_SELECTED);
        intent.putExtra(EXTRA_SELECTED_WEEK,m_week);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private class FestivalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                int resId = intent.getIntExtra(EXTRA_FESTIVAL_RES, 1);
                ll_schedule.setBackgroundResource(resId);
                m_resid = resId;
            }
        }
    }
}