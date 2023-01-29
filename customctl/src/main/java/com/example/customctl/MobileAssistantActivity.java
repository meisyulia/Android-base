package com.example.customctl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.customctl.adapter.TrafficInfoAdapter;
import com.example.customctl.bean.AppInfo;
import com.example.customctl.bean.StringUtil;
import com.example.customctl.util.AppUtil;
import com.example.customctl.util.DateUtil;
import com.example.customctl.util.SharedUtil;
import com.example.customctl.widget.CircleAnimation;
import com.example.customctl.widget.CustomDialog;
import com.example.customctl.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.Calendar;

import service.TrafficService;

public class MobileAssistantActivity extends AppCompatActivity implements View.OnClickListener, CustomDialog.OnDateSetListener {

    private TextView tv_day;
    private RelativeLayout rl_month;
    private TextView tv_month_traffic;
    private RelativeLayout rl_day;
    private TextView tv_day_traffic;
    private NoScrollListView nslv_traffic;
    private int limit_month;
    private int limit_day;
    private int mDay;
    private int mNowDay;
    private int traffic_day;
    private int line_width =10; //圆弧线宽

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_assistant);
        //创建一个通往流量服务的意图
        Intent intent = new Intent(this, TrafficService.class);
        //启动指定意图的服务
        startService(intent);
        initView();
    }

    //初始化视图对象
    private void initView() {
        tv_day = (TextView) findViewById(R.id.tv_day);
        rl_month = (RelativeLayout) findViewById(R.id.rl_month);
        tv_month_traffic = (TextView) findViewById(R.id.tv_month_traffic);
        rl_day = (RelativeLayout) findViewById(R.id.rl_day);
        tv_day_traffic = (TextView) findViewById(R.id.tv_day_traffic);
        nslv_traffic = (NoScrollListView) findViewById(R.id.nslv_traffic);
        findViewById(R.id.iv_menu).setOnClickListener(this);
        findViewById(R.id.iv_refresh).setOnClickListener(this);
        //共享参数中读取月流量限额和日流量限额
        limit_month = SharedUtil.getInstance(this).readInt("limit_month", 1024);
        limit_day = SharedUtil.getInstance(this).readInt("limit_day", 30);
        mNowDay = Integer.parseInt(DateUtil.getNowDateTime("yyyyMMdd"));
        mDay = mNowDay;
        String day = DateUtil.getNowDateTime("yyyy年MM月dd日");
        tv_day.setText(day);
        tv_day.setOnClickListener(this);
        //延迟500毫秒开始刷新日流量数据
        mHandler.postDelayed(mDayRefresh,500);
    }

    private Handler mHandler = new Handler();
    private Runnable mDayRefresh = new Runnable() {
        @Override
        public void run() {
            refreshTraffic(mDay);
        }
    };

    //刷新指定日期的流量数据
    private void refreshTraffic(int day) {
        String last_date = DateUtil.getAddDate("" + day, -1);
        //计算今日的流量数据
        ArrayList<AppInfo> lastArray = MainApplication.getInstance().mTrafficHelper.query("day=" + last_date);
        ArrayList<AppInfo> thisArray = MainApplication.getInstance().mTrafficHelper.query("day=" + day);
        ArrayList<AppInfo> newArray = new ArrayList<>();
        traffic_day = 0;
        for (int i = 0; i < thisArray.size(); i++) {
            AppInfo item = thisArray.get(i);
            for (int j = 0; j < lastArray.size(); j++) {
                if (item.uid == lastArray.get(j).uid){
                    item.traffic -= lastArray.get(j).traffic;
                    break;
                }
            }
            traffic_day+= item.traffic;
            newArray.add(item);
        }
        //给流量信息对了补充每个应用的图标
        ArrayList<AppInfo> fullArray = AppUtil.fillAppInfo(this, newArray);
        TrafficInfoAdapter adapter = new TrafficInfoAdapter(MobileAssistantActivity.this, fullArray);
        nslv_traffic.setAdapter(adapter);
        showDayAnimation(); //显示日流量动画
        showMonthAnimation(); //显示月流量动画

    }

    //显示日流量的圆弧动画
    private void showDayAnimation() {
        rl_day.removeAllViews();
        //计算圆弧的内直径
        int diameter = Math.min(rl_day.getWidth(),rl_day.getHeight())-line_width*2;
        String desc = "今日已用流量"+ StringUtil.formatData(traffic_day);
        //创建圆弧动画
        CircleAnimation dayAnimation = new CircleAnimation(MobileAssistantActivity.this);
        //设置动画的上下左右坐标
        dayAnimation.setRect((rl_day.getWidth()-diameter)/2+line_width,(rl_day.getHeight()-diameter)/2+line_width,
                (rl_day.getWidth()+diameter)/2-line_width,(rl_day.getHeight()+diameter)/2-line_width);
        float trafficM = traffic_day / 1024.0f / 1024.0f;
        if (trafficM>limit_day*2){
            int end_angle = (int)((trafficM>limit_day*3)?360:(trafficM-limit_day*2)*360/limit_day);
            dayAnimation.setAngle(0,end_angle);
            dayAnimation.setFront(Color.RED,line_width, Paint.Style.STROKE);
            desc = String.format("%s\n超出限额%s"+desc,
                    StringUtil.formatData(traffic_day-limit_day*1024*1024));
        }else if(trafficM>limit_day){
            int end_angle = (int)((trafficM>limit_day*2)?360:(trafficM-limit_day)*360/limit_day);
            dayAnimation.setAngle(0,end_angle);
            dayAnimation.setFront(0xffff9900,line_width, Paint.Style.STROKE);
            desc = String.format("%s\n超出限额%s"+desc,
                    StringUtil.formatData(traffic_day-limit_day*1024*1024));
        }else{
            int end_angle = (int) (trafficM*360/limit_day);
            dayAnimation.setAngle(0,end_angle);
            dayAnimation.setFront(Color.GREEN,line_width, Paint.Style.STROKE);
            desc = String.format("%s\n剩余流量%s",desc,
                    StringUtil.formatData(limit_day*1024*1024-traffic_day));
        }
        rl_day.addView(dayAnimation);
        dayAnimation.render();
        tv_day_traffic.setText(desc);
    }

    // 显示月流量的圆弧动画。未实现，读者可实践之
    private void showMonthAnimation() {
        rl_month.removeAllViews();
        int diameter = Math.min(rl_month.getWidth(), rl_month.getHeight()) - line_width * 2;
        tv_month_traffic.setText("本月已用流量待统计");
        // 创建月流量的圆弧动画
        CircleAnimation monthAnimation = new CircleAnimation(MobileAssistantActivity.this);
        // 设置月流量动画的四周边界
        monthAnimation.setRect((rl_month.getWidth() - diameter) / 2 + line_width,
                (rl_month.getHeight() - diameter) / 2 + line_width,
                (rl_month.getWidth() + diameter) / 2 - line_width,
                (rl_month.getHeight() + diameter) / 2 - line_width);
        monthAnimation.setAngle(0, 0);
        monthAnimation.setFront(Color.GREEN, line_width, Paint.Style.STROKE);
        rl_month.addView(monthAnimation);
        // 渲染月流量的圆弧动画
        monthAnimation.render();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_day){
            Calendar calendar = Calendar.getInstance();
            //弹出自定义的日期选择对话框
            CustomDialog dialog = new CustomDialog(this);
            dialog.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY),
                    calendar.get(Calendar.DAY_OF_MONTH),this);
            dialog.show();
        }else if(v.getId() == R.id.iv_menu){
            //跳转到流量限额配置页面
            Intent intent = new Intent(this, MobileConfigActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.iv_refresh){
            mDay = mNowDay;
            //立即启动今日的流量刷新任务
            mHandler.post(mDayRefresh);
        }
    }

    @Override
    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {
        String date = String.format("%d年%d月%d日", year, monthOfYear, dayOfMonth);
        tv_day.setText(date);
        mDay = year*10000+monthOfYear*100+dayOfMonth;
        //选择完日期立即刷新流量任务
        mHandler.post(mDayRefresh);
    }
}