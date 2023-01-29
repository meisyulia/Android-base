package com.example.customctl.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedUtil {

    private static SharedUtil mUtil;
    private static SharedPreferences mShared;

    //通过单例模式获取共享参数工具类的唯一实例
    public static SharedUtil getInstance(Context context){
        if (mUtil == null) {
            mUtil = new SharedUtil();
        }
        //从share.xml中获取共享参数对象
        mShared = context.getSharedPreferences("share", Context.MODE_PRIVATE);
        return mUtil;
    }

    public void writeString(String key,String value){
        SharedPreferences.Editor edit = mShared.edit();
        edit.putString(key,value);
        edit.commit();
    }

    public String readString(String key,String defaultValue){
        return mShared.getString(key,defaultValue);
    }

    public void writeInt(String key,int value){
        SharedPreferences.Editor edit = mShared.edit();
        edit.putInt(key,value);
        edit.commit();
    }

    public int readInt(String key,int defaultValue){
        return mShared.getInt(key,defaultValue);
    }
}
