package com.example.myapplication.util;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;

public class ViewUtil {



    public static int getMaxLength(EditText et){
        int length = 0;

        try {
            InputFilter[] inputFilters = et.getFilters();
            for (InputFilter filter : inputFilters) {
                Class<? extends InputFilter> c = filter.getClass();
                if (c.getName().equals("android.text.InputFilter$LengthFilter")){
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if(field.getName().equals("mMax")){
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return length;
    }

    public static void hideAllInputMethod(Activity act){
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        //软键盘如果已经打开则关闭之
        if(imm.isActive()==true){
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideOneInputMethod(Activity act, View v){
        //实际上不只是et_other的软键盘会关闭，其它编辑框的软键盘也会关闭
        //因为方法内部去获取视图的WindowToken，这个Token在每个页面上都是唯一的
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}
