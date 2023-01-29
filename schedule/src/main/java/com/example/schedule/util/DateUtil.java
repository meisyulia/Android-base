package com.example.schedule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    //获取日期和时间
    public static String getNowDateTime(String formatStr){
        String format = formatStr;
        if (format == null || format.length()<=0){
            format = "yyyyMMddHHmmss";
        }
        SimpleDateFormat s_format = new SimpleDateFormat(format);
        return s_format.format(new Date());
    }

    //获取时间
    public static String getNowTime(){
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss");
        return s_format.format(new Date());
    }

    //获取详细时间
    public static String getNowTimeDetail(){
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss:SSS");
        return s_format.format(new Date());
    }

    //获取当前日期
    public static String getNowDate(){
        SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMdd");
        return s_format.format(new Date());
    }

    //获取当前年份
    public static String getNowYearCN(){
        SimpleDateFormat s_format = new SimpleDateFormat("yyyy年");
        return s_format.format(new Date());
    }

    //获取当前月份
    public  static int getNowMonth(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH)+1;
    }

    //获取增加天数后的日期
    public static String getAddDate(String str,long day_num){
        SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMdd");
        Date old_date = null;
        try {
            old_date = s_format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //转换为毫秒数
        long time = old_date.getTime();
        long diff_time = day_num * 24 * 60 * 60 * 1000;
        time +=diff_time;
        Date new_date = new Date(time);
        return s_format.format(new_date);
    }

    //获取对应日期对应的星期几
    public static int getWeekIndex(String s_date){
        SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMdd");
        Date d_date = null;
        try {
            d_date = s_format.parse(s_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d_date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index == 0){
            week_index = 7;
        }
        return week_index;
    }

    //检查是否是假期
    public static boolean checkHoliday(String text){
        boolean result = true;
        if((text.length()==2 && (text.indexOf("月")>0
            || text.indexOf("初")>=0
            || text.indexOf("十")>=0
            || text.indexOf("廿")>=0
            || text.indexOf("卅")>=0))
            || (text.length()==3 && text.indexOf("月")>0)){
           result = false;
        }
        return result;
    }
}
