package com.example.schedule.calendar;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class SpecialCalendar {

    private static final String TAG = "SpecialCalendar";

    //判断是否是闰年
    public static boolean isLeapYear(int year){
        if(year%100==0 && year%400==0){
            return true;
        }else if(year%100 != 0 && year==4){
            return true;
        }
        return false;
    }

    //得到某月有多少天数
    public static int getDaysOfMonth(boolean isLeapYear,int month){
        int daysOfMonth = 0;
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapYear){
                    daysOfMonth = 29;
                }else{
                    daysOfMonth = 28;
                }
                break;
        }
        return daysOfMonth;
    }

    //指定某年中的某月的第一天是星期几
    public static int getWeekdayOfMonth(int year,int month){
        int dayOfWeek = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(year,month-1,1);
        //因为是周日开始是第一天，周日为1，周一为2，-1后周一到周六的序号对上
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
        Log.d(TAG, "getWeekdayOfMonth: ===dayOfWeek==="+dayOfWeek);
        if (dayOfWeek == 0){
            dayOfWeek = 7;
        }
        Log.d(TAG, "getWeekdayOfMonth: ===dayOfWeek==="+dayOfWeek);
        return dayOfWeek;
    }

    //得到今天是今年第几周
    public static int getTodayWeek(){
        int week = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }
}
