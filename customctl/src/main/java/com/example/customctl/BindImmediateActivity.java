package com.example.customctl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.customctl.util.DateUtil;

import service.BindImmediateService;

public class BindImmediateActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BindImmediateActivity";
    private static TextView tv_immediate;
    private static String mDesc="";
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_immediate);
        tv_immediate = findViewById(R.id.tv_immediate);
        findViewById(R.id.btn_start_bind).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        //创建立即绑定服务的意图
        mIntent = new Intent(this, BindImmediateService.class);

    }

    public static void showText(String desc) {
        if (tv_immediate != null) {
            mDesc = String.format("%s%s %s\n", mDesc, DateUtil.getNowDateTime("HH:mm:ss"), desc);
            tv_immediate.setText(mDesc);
        }
    }

    private BindImmediateService mBindService;
    private ServiceConnection mFirstConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBindService = ((BindImmediateService.LocalBinder) iBinder).getService();
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBindService = null;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_start_bind){
            boolean bindFlag = bindService(mIntent, mFirstConn, Context.BIND_AUTO_CREATE);
            Log.d(TAG, "onClick: bindFlag="+bindFlag+",mBindService="+mBindService);
        }else if(v.getId() == R.id.btn_unbind){
            if (mBindService != null){
                unbindService(mFirstConn);
                mBindService=null;
            }
        }
    }
}