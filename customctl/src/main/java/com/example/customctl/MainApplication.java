package com.example.customctl;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.example.customctl.database.TrafficDBHelper;
import com.example.customctl.util.NotifyUtil;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    //声明一个当前应用的静态实例
    private static MainApplication mApp;
    public TrafficDBHelper mTrafficHelper;

    //利用单例模式获取当前应用的唯一实例
    public static MainApplication getInstance(){
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //在打开应用时对静态的应用实例赋值
        mApp = this;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotifyUtil.createNotifyChannel(this,getString(R.string.app_name));
        }
        //获得流量数据库帮助器的实例
        mTrafficHelper = TrafficDBHelper.getInstance(this, 1);
        //写连接打开
        mTrafficHelper.openWriteLink();
        Log.d(TAG, "onCreate: ");
    }
}
