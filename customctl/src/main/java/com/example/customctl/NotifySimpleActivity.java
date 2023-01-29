package com.example.customctl;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

public class NotifySimpleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NotifySimpleActivity";
    private EditText et_title;
    private EditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_simple);
        et_title = (EditText) findViewById(R.id.et_title);
        et_message = (EditText) findViewById(R.id.et_message);
        findViewById(R.id.btn_send_simple).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_send_simple){
            String title = et_title.getText().toString();
            String message = et_message.getText().toString();
            sendSimpleNotify(title,message);
        }
    }

    private void sendSimpleNotify(String title, String message) {
        Intent clickIntent = new Intent(this, NotifySimpleActivity.class);
        //用于页面跳转的延迟意图
        PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name,
                clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建一个通知消息的构造器
        Notification.Builder builder = new Notification.Builder(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new Notification.Builder(this,getString(R.string.app_name));
        }
        builder.setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_app)
                .setTicker("提示消息来啦~")
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_app))
                .setContentTitle(title)
                .setContentText(message);
        Notification notification = builder.build();
        //从系统服务中获取通知管理器
        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //需要先判断是否有开通知栏权限，有发消息，没有则跳转到通知栏开启权限
        if(NotificationManagerCompat.from(this).areNotificationsEnabled()){
            notifyMgr.notify(R.string.app_name,notification);
        }else{
            //跳转到通知栏界面
            AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("通知栏权限")
                    .setMessage("尚未开启通知权限，请点击去进行开启")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                            //intent.putExtra("app_package", getPackageName());
                            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                           /* intent.putExtra("app_uid", getApplicationInfo().uid);
                            Log.d(TAG, "onClick: app_package=" + getPackageName() + ",app_uid=" + getApplicationInfo().uid);*/
                            //运行的时候提示在已安装的应用中找不到该应用 ,传的数据信息不对
                            startActivity(intent);
                            Toast.makeText(NotifySimpleActivity.this, "已发送通知消息", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //提示尚未开通权限
                            Toast.makeText(NotifySimpleActivity.this, "通知栏权限尚未开启，请先开启权限", Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            alertDialog.show();
        }
    }
}