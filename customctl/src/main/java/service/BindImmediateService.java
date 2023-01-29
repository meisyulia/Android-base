package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.customctl.BindImmediateActivity;

public class BindImmediateService extends Service {

    private static final String TAG = "BindImmediateService";
    private final IBinder mBinder = new LocalBinder();

    //定义一个当前服务的粘合剂，用于该服务粘到活动页面的进程中
    public class LocalBinder extends Binder{
        public BindImmediateService getService(){
            return BindImmediateService.this;
        }
    }
    @Override
    public void onCreate() { // 创建服务
        refresh("onCreate");
        super.onCreate();
    }

    private void refresh(String text) {
        Log.d(TAG, "refresh: text="+text);
        BindImmediateActivity.showText(text);
    }

    @Override
    public void onStart(Intent intent, int startid) { // 启动服务
        refresh("onStart");
        super.onStart(intent, startid);
    }

    @Override
    public void onDestroy() { // 销毁服务
        refresh("onDestroy");
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        refresh("onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) { // 重新绑定服务
        refresh("onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) { // 解绑服务。返回false表示只能绑定一次，返回true表示允许多次绑定
        Log.d(TAG, "绑定服务结束旅程！");
        refresh("onUnbind");
        return true;
    }
}
