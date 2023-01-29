package com.example.schedule.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.schedule.R;
import com.example.schedule.activity.ScheduleActivity;
import com.example.schedule.adapter.CalendarGridAdapter;
import com.example.schedule.adapter.ScheduleListAdapter;
import com.example.schedule.bean.CalendarTransfer;
import com.example.schedule.bean.ScheduleArrange;
import com.example.schedule.calendar.Constant;
import com.example.schedule.calendar.SpecialCalendar;
import com.example.schedule.database.DbHelper;
import com.example.schedule.database.ScheduleArrangeHelper;
import com.example.schedule.util.DateUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ScheduleFragment";
    private FragmentActivity mContext;
    private int m_week;
    private View mView;
    private int m_nowWeek;
    private int year_c;
    private int month_c;
    private int day_c;
    private String thisDate;
    private int jumpYear;
    private int jumpMonth;
    private int first_pos;
    private CalendarGridAdapter cal;
    private ArrayList<CalendarTransfer> transArray = new ArrayList<>();
    private ListView lv_schedule;
    private ScrollControlReceiver scrollControlReceiver;
    private ScheduleArrangeHelper arrangeHelper;
    private ArrayList<ScheduleArrange> arrangeList;

    // TODO: Rename and change types of parameters
   /* private String mParam1;
    private String mParam2;*/

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param seq Parameter 1.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(int seq) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt("week",seq);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        if (getArguments() != null){
            m_week = getArguments().getInt("week", 1);
        }
        m_nowWeek = SpecialCalendar.getTodayWeek();
        initDate(m_week-m_nowWeek);
        Log.d(TAG, "onCreateView: jumpYear="+jumpYear+" jumpMonth="+jumpMonth+" year_c="+year_c+" month_c="+month_c+" day_c="+day_c);
        cal = new CalendarGridAdapter(mContext, jumpYear, jumpMonth, year_c, month_c, day_c);
        for (int i = first_pos; i <first_pos+7 ; i++) {
            CalendarTransfer trans = cal.getCalendarList(i);
            Log.d(TAG, "i="+i+",trans.solar_month="+trans.solar_month+" trans.solar_day="+trans.solar_day
            +" trans.lunar_month="+trans.lunar_month+" trans.lunar_day="+trans.lunar_day);
            transArray.add(trans);
        }
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_schedule, container, false);
        lv_schedule = (ListView) mView.findViewById(R.id.lv_schedule);
        return mView;
    }

    //要初始化数据到列表视图中：
    //1.跳转到当前的日期的周期，并初始化视图（需要的数据有对应一周的日历日期信息，对应一周的日程安排信息）
    // 2.初始化的数据有当前周的周一到周日对应的日历日期
    // 2.1.获取当前周对应月份的日历日期：
    //      通过当前的周数知道跳转了多少周（ps:刚刚初始化是跳转了0周）间接知道跳转年份的次数，跳转月份的次数，本身知道系统当前年份，月份和时间
    // 2.2.如何获取当前周对应的一周的日历日期？
    //     2.2.1 首先要获取当前周一对应的日历网格图上的子项目的下标，连续7项就是对应的日历日期
    //  如何知道当前周一的下标，可以计算一下当天是这个月的第几周，前几周*7 = 当前周的星期一的下标
    // 当前的日期-当前日期对应的星期几 = 上周日的本月日期（正整数的时候）(有人认为周日是一周第一天，我这是周日是一周最后一天）
    private void initDate(int diff_weeks) {
        String currentDate = DateUtil.getNowDateTime("yyyy-MM-dd");
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        //通过跳转了多少周得知跳转后的的日期
        String nowDate = DateUtil.getNowDate();
        thisDate = DateUtil.getAddDate(nowDate, diff_weeks * 7);
        int thisYear = Integer.parseInt(thisDate.substring(0, 4));
        int thisMonth = Integer.parseInt(thisDate.substring(4, 6));
        int thisDay = Integer.parseInt(thisDate.substring(6, 8));
        jumpYear = thisYear - year_c; //数值：跳转的次数
        if(jumpYear<0){
            //跳转到上一年
            jumpMonth = thisMonth - 12 - month_c;
        }else if(jumpYear==0){
            jumpMonth= thisMonth - month_c;
        }else{
            //跳转到下一年
            jumpMonth = thisMonth + 12 -month_c;
        }
        //计算出当天是本月第几周-》前几周*7 = 当天第几周的周一在日历日期中的下标-》可以已知的有当天的日期，当天是星期几
        int weekIndex = DateUtil.getWeekIndex(thisDate);
        //计算前几周
        int week_count = (thisDay - weekIndex) / 7;
        if((thisDay-weekIndex)%7>0){
            week_count++;
        }
        if(thisDay-week_count<0){
            week_count++;
        }
        //当天对应的那周的周一在日历日期中的下标
        first_pos = week_count * 7;
        Log.d(TAG, "initDate: first_pos="+first_pos+" thisDate="+thisDate);
    }

    //接收来自活动页中被选中的碎片页面的信息
    @Override
    public void onStart() {
        super.onStart();
        scrollControlReceiver = new ScrollControlReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(scrollControlReceiver,
                new IntentFilter(ScheduleActivity.ACTION_FRAGMENT_SELECTED));
        //需要打开数据库的存储日程表的信息，将对应7天的日程信息查询出来
        //获取对应的7天的开始日期和结束日期
        CalendarTransfer begin_trans = transArray.get(0);
        String begin_day = String.format("%s%02d%02d", begin_trans.solar_year, begin_trans.solar_month, begin_trans.solar_day);
        CalendarTransfer end_trans = transArray.get(transArray.size() - 1);
        String end_day = String.format("%s%02d%02d", end_trans.solar_year, end_trans.solar_month, end_trans.solar_day);
        arrangeHelper = new ScheduleArrangeHelper(mContext, DbHelper.db_name, null, 1);
        arrangeList = (ArrayList<ScheduleArrange>) arrangeHelper.queryInfoByDayRange(begin_day, end_day);
        ScheduleListAdapter listAdapter = new ScheduleListAdapter(mContext, transArray, arrangeList);
        lv_schedule.setAdapter(listAdapter);
        lv_schedule.setOnItemClickListener(listAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(scrollControlReceiver);
        //关闭数据库
        arrangeHelper.close();
    }

    private class ScrollControlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                int selectedWeek = intent.getIntExtra(ScheduleActivity.EXTRA_SELECTED_WEEK, 1);
                Log.d(TAG, "onReceive: selectedWeek="+selectedWeek+", m_week="+m_week);
                if (m_week == selectedWeek){
                    startRefresh();
                }
            }
        }
    }

    private void startRefresh() {
        //当滑动到的周几和页面发过来的第几周对应上则检查是否有节假日，如果有节假日则发送广播到活动页，更换活动页的背景
        checkFestival();
    }

    private void checkFestival() {
        int i = 0;
        for (; i < transArray.size(); i++) {
            CalendarTransfer trans = transArray.get(i);
            int j = 0;
            for (; j < Constant.festivalArray.length; j++) {
                if(trans.day_name.indexOf(Constant.festivalArray[j]) >=0){
                    //把对应的节日的背景资源发送给活动页(跳出两层循环）
                    sendFestival(Constant.festivalResArray[j]);
                    break;
                }
            }
            if(j<Constant.festivalArray.length){
                break;
            }
        }
        //如果没有节日，则背景使用普通的页面
        if (i>=transArray.size()){
            sendFestival(R.drawable.normal_day);
        }
    }

    private void sendFestival(int resId) {
        //将对应的节日背景资源发送到活动页
        Intent intent = new Intent(ScheduleActivity.ACTION_SHOW_FESTIVAL);
        intent.putExtra(ScheduleActivity.EXTRA_FESTIVAL_RES,resId);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}