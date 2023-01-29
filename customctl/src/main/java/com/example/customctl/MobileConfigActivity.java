package com.example.customctl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.customctl.util.SharedUtil;

public class MobileConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MobileConfigActivity";
    private EditText et_config_month;
    private EditText et_config_day;
    private String mCustomNumber="10086"; //中国移动客服电话
    private Uri mSmsUri;
    private String[] mSmsColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_config);
        et_config_month = (EditText) findViewById(R.id.et_config_month);
        et_config_day = (EditText) findViewById(R.id.et_config_day);
        findViewById(R.id.btn_config_save).setOnClickListener(this);
        /*findViewById(R.id.btn_auto_adjust).setOnClickListener(this);*/
        int limit_month = SharedUtil.getInstance(this).readInt("limit_month", 1024);
        int limit_day = SharedUtil.getInstance(this).readInt("limit_day", 50);
        et_config_month.setText(""+limit_month);
        et_config_day.setText(""+limit_day);
        /*//初始化短信的内容观察器
        initSmsObserver();*/
    }
/*
    private Handler mHandler = new Handler();
    //初始化短信观察器
    private void initSmsObserver() {
        mSmsUri = Uri.parse("content://sms");
        mSmsColumn = new String[]{"address", "body", "date"};
        mObserver = new SmsGetObserver(this, mHandler);
        //给指定Uri注册内容观察器，一旦Uri内部发生数据变化，就触发观察器的onChange方法
        getContentResolver().registerContentObserver(mSmsUri,true,mObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }*/

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_config_save){
            //将流量限额保存到共享参数中
            saveFlowConfig(Integer.parseInt(et_config_month.getText().toString()),
                    Integer.parseInt(et_config_day.getText().toString()));
            Toast.makeText(this, "成功保存配置", Toast.LENGTH_SHORT).show();
            finish();
        }/*else if(v.getId() == R.id.btn_auto_adjust){
            //无需用户操作，自动发送短信 现在的校准流量短信好像不是发18了
            sendSmsAuto(mCustomNumber,"18");
        }*/
    }

    //将每月和每日的流量限额保存到共享参数中
    private void saveFlowConfig(int limit_month, int limit_day) {
        SharedUtil.getInstance(MainApplication.getInstance()).writeInt("limit_month",limit_month);
        SharedUtil.getInstance(MainApplication.getInstance()).writeInt("limit_day",limit_day);
    }

    /*//短信发送事件
    private String SENT_SMS_ACTION = "com.example.customctl.SENT_SMS_ACTION";
    //短信接收事件
    private String DELIVERED_SMS_ACTION = "com.example.customctl.DELIVERED_SMS_ACTION";
    //自动发送短信
    private void sendSmsAuto(String phoneNumber, String message) {
        //指定短信发送事件的详细信息
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        sentIntent.putExtra("phone",phoneNumber);
        sentIntent.putExtra("message",message);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //接收
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        deliverIntent.putExtra("phone",phoneNumber);
        deliverIntent.putExtra("message",message);
        PendingIntent deliverPI = PendingIntent.getBroadcast(this, 1, deliverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //获取短信管理器
        SmsManager smsManager = SmsManager.getDefault();
        //开始发哦送那个短信内容。要确保打开发送短信的完全权限，不是那种还需要提示的不完整权限
        smsManager.sendTextMessage(phoneNumber,null,message,sentPI,deliverPI);
    }



    private class SmsGetObserver extends ContentObserver {

        private final Context mContext; //声明一个上下文对象

        public SmsGetObserver(Context context, Handler handler) {
            super(handler);
            mContext = context;
        }

        @Override
        public void onChange(boolean selfChange) { //观察短信内容提供器发生变化
            String sender = "",content="";
            super.onChange(selfChange);
            //构建一个查询短信的条件语句
            String selection = String.format("address='%s' and date>%d", mCustomNumber, System.currentTimeMillis() - 1000 * 60 * 60);
            //通过内容解析器获取符合条件的结果急游标
            Cursor cursor = mContext.getContentResolver().query(mSmsUri, mSmsColumn, selection, null, "date desc");
            //取出最新的短信记录
            while (cursor.moveToNext()){
                sender = cursor.getString(0);
                content = cursor.getString(1);
                Log.d(TAG, "onChange: sender="+sender+",content="+content);
                break;
            }
            cursor.close();
            //content = "您办理的套餐内含数据总流量为1GB176MB，已使用310MB，剩余890MB。";
            String totalFlow = "0";
            if (sender.equals(mCustomNumber)){
                //解析流量校准短信里面的总流量数值
                totalFlow = findFlow(content,"总流量为","，");
            }
            String[] flows = totalFlow.split("GB");
            Log.d(TAG, "onChange: totalFlow="+totalFlow+",flows.length="+flows.length);

        }
    }*/
}