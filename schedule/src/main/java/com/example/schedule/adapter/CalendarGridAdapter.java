package com.example.schedule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.bean.CalendarTransfer;
import com.example.schedule.calendar.LunarCalendar;
import com.example.schedule.calendar.SpecialCalendar;
import com.example.schedule.util.DateUtil;

import java.util.ArrayList;

public class CalendarGridAdapter extends BaseAdapter {

    private static final String TAG = "CalendarGridAdapter";
    private final Context mContext;
    private final String sysDate; //当前系统时间
    private final LunarCalendar lc;
    private final String currentYear;
    private final String currentMonth;
    private final String currentDay;
    private final String sys_year;
    private final String sys_month;
    private final String sys_day;
    private boolean isLeapYear;
    private int daysOfMonth;
    private int dayOfWeek;
    private int lastDayOfMonth;
    private int currentFlag = -1; //用于标记当天
    private ArrayList<CalendarTransfer> transArray = new ArrayList<>();

    public CalendarGridAdapter(Context context, int jumpYear, int jumpMonth, int year_c, int month_c, int day_c){
        mContext = context;
        sysDate = DateUtil.getNowDateTime("yyyy-M-d");
        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];
        lc = new LunarCalendar();
        int stepYear = year_c + jumpYear;
        int stepMonth = month_c + jumpMonth;
        Log.d(TAG, "stepMonth before: "+stepMonth);
        //当前的年份： 跳年份的次数：  当前的月份：  跳月份的次数（向右滑+1，向左滑-1）
       /* Log.d(TAG, "当前的年份： "+year_c+" 跳年份的次数："+jumpYear+" 当前的月份:"+month_c+" 跳月份的次数:"+jumpMonth+" 当前日期："+day_c);*/
        if (stepMonth > 0){
            //往今年的月份滑动
            if(stepMonth%12==0){
                stepYear = year_c + stepMonth/12 -1;
                stepMonth = 12;
            }else{
                stepYear = year_c +stepMonth/12;
                stepMonth = stepMonth%12;
            }
        }else{
            //往去年的月份滑动
            stepYear = year_c-1 +stepMonth/12;
            stepMonth = stepMonth%12 +12;
        }
        Log.d(TAG, "系统的年份： "+sys_year+" 跳年份的次数："+jumpYear+" 系统的月份:"+sys_month+" 跳月份的次数:"+jumpMonth+" 系统日期："+sys_day);
        Log.d(TAG, "当前的年份： "+year_c+" 跳年份的次数："+jumpYear+" 当前的月份:"+month_c+" 跳月份的次数:"+jumpMonth+" 当前日期："+day_c);
        //得到滑动后的年份,月份和当前日期
        currentYear = String.valueOf(stepYear);
        currentMonth = String.valueOf(stepMonth);
        currentDay = String.valueOf(day_c);
        Log.d(TAG, "滑动后的年份和月份: "+currentYear+" "+currentMonth);
        //某年某月的天数和该月第一天是星期几
        getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
    }

    //得到某年的某月的天数且这月的第一天是星期几
    public void getCalendar(int year, int month) {
        isLeapYear = SpecialCalendar.isLeapYear(year);
        //某月的总天数
        daysOfMonth = SpecialCalendar.getDaysOfMonth(isLeapYear, month);
        //某月的第一天为星期几
        dayOfWeek = SpecialCalendar.getWeekdayOfMonth(year, month);
        //上个月的总天数
        //如果这个月是1月，上个月是去年12月
        int last_month = month - 1;
        if(last_month<=0){
            last_month=12;
        }
        lastDayOfMonth = SpecialCalendar.getDaysOfMonth(isLeapYear, last_month);
        Log.d(TAG, "isLeapYear  daysOfMonth dayOfWeek lastDayOfMonth === "+isLeapYear+" "+daysOfMonth+" "+dayOfWeek+" "+lastDayOfMonth);
        getWeek(year,month);
    }

    private String[] dayNumber = new String[49]; //一个gridView中的日期存在此数组中
    private static String[] week = {"周一","周二","周三","周四","周五","周六", "周日"};
    //将一个月中的每一天的值添加到数组dayNumber中
    private void getWeek(int year, int month) {
        int j=1;
        String lunarDay = "";
        for (int i = 0; i < dayNumber.length; i++) {
            CalendarTransfer transfer = new CalendarTransfer();
            //对应表格项的周几
            int weekday = (i - 7) % 7 + 1;
            //第一列0~6 表示的周一到周日
            if (i<7){
                dayNumber[i] = week[i]+"."+" ";
            }else if(i<dayOfWeek+7-1) { // 前一个月  dayOfWeek表示第一天是星期几 dayOfWeek+7-1是本月第一天的表格项序号（从0开始，要-1）
                int last_month = month-1;
                int last_year = year;
                if(last_month<=0){
                    last_month=12;
                    last_year--;
                }
                int temp = lastDayOfMonth - dayOfWeek + 1 - 7 + 1;
                transfer = lc.getSubDate(transfer, last_year, last_month, temp + i, weekday, false);
                lunarDay = transfer.day_name;
                dayNumber[i] = (temp+i)+"."+lunarDay;
            }else if(i<daysOfMonth+dayOfWeek+7-1){ //本月
                int temp = i-dayOfWeek+1-7+1;
                //得到对应的日期
                String day = String.valueOf(temp);
                transfer = lc.getSubDate(transfer, year, month, temp, weekday, false);
                lunarDay = transfer.day_name;;
                dayNumber[i] = temp+"."+lunarDay;
                //如果当前的日期和系统当前的日期对应上，进行标记对应的表格项
                if(sys_year.equals(String.valueOf(year))&& sys_month.equals(String.valueOf(month))&& sys_day.equals(day)){
                    currentFlag = i;
                }
            }else{
                //下个月
                int next_month = month + 1;
                int next_year = year;
                if(next_month>=13){
                    next_month=1;
                    next_year++;
                }
                transfer = lc.getSubDate(transfer,next_year,next_month,j,weekday,false);
                lunarDay = transfer.day_name;
                dayNumber[i] = j+"."+lunarDay;
                j++;
            }
            //跳过前7个
            if(i>=7){
                transArray.add(transfer);
            }
        }
        //打日志输出数组dayNumber
        String abc = "";
        for (int i = 0; i < dayNumber.length; i++) {
            abc = abc+dayNumber[i]+":";
        }
        Log.d(TAG, "getWeek: dayNumber==="+abc);
        //打日志输出transArray
        String logb = "";
        for (int i = 0; i < transArray.size(); i++) {
            logb = String.format("%s %d:solar_month=%s,solar_day=%s  ",logb,i,transArray.get(i).solar_month,transArray.get(i).solar_day);
        }
        Log.d(TAG, "getWeek: transArray==="+logb);
    }

    public CalendarTransfer getCalendarList(int pos){
        return transArray.get(pos);
    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_calendar,null);
        }
        TextView tv_day = (TextView) convertView.findViewById(R.id.tv_day);
        String day = dayNumber[position].split("\\.")[0];
        String festival = dayNumber[position].split("\\.")[1];
        String itemText = day;
        if(position>=7){
            itemText = itemText+"\n"+festival;
        }
        tv_day.setText(itemText);
        tv_day.setTextColor(Color.GRAY);
        if (position<7){
            //设置周的字体颜色和背景颜色
            tv_day.setTextColor(Color.BLACK);
            tv_day.setBackgroundColor(Color.LTGRAY);
        }else{
            tv_day.setBackgroundColor(Color.WHITE);
        }
        if(position>=dayOfWeek+7-1 && position<daysOfMonth+dayOfWeek+7-1){
            //当前月的信息显示
            if(DateUtil.checkHoliday(festival) == true){
                //节日字体标蓝
                tv_day.setTextColor(Color.BLUE);
            }else if((position+1)%7==6 || (position+1)%7 == 0){
                //周末字体标红
                tv_day.setTextColor(Color.RED);
            }else{
                //当月字体标黑
                tv_day.setTextColor(Color.BLACK);
            }
        }
        if (currentFlag == position){
            //设置当天的背景为蓝色
            tv_day.setBackgroundColor(Color.parseColor("#0078d7"));
        }
        return convertView;
    }
}
