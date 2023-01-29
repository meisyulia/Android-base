package com.example.myapplication.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getNowDateTime(){
        SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMddhhmmss");
        return s_format.format(new Date());
    }

    public static String getNowTime(){
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss");
        return s_format.format(new Date());
    }

    public static String getNowTimeDetail(){
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss.SSS");
        return s_format.format(new Date());
    }
}
