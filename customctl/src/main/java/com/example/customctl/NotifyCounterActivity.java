package com.example.customctl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

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

public class NotifyCounterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NotifyCounterActivity";
    private EditText et_title;
    private EditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_counter);
        et_title = (EditText) findViewById(R.id.et_title);
        et_message = (EditText) findViewById(R.id.et_message);
        findViewById(R.id.btn_send_counter).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send_counter){
            String title = et_title.getText().toString();
            String message = et_message.getText().toString();
            sendCounterNotify(title,message);
        }
    }

    private void sendCounterNotify(String title, String message) {
        Intent cancelIntent = new Intent(this, NotifyCounterActivity.class);
        PendingIntent deleteIntent = PendingIntent.getActivity(this, R.string.app_name, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            builder = new Notification.Builder(this,getString(R.string.app_name));
        }
        builder.setDeleteIntent(deleteIntent) // 设置内容的清楚意图
                .setAutoCancel(true)
                .setUsesChronometer(true)
                .setProgress(100,60,false)
                .setNumber(99)
                .setSmallIcon(R.drawable.ic_app)
                .setTicker("提示消息来啦~")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_app))
                .setContentTitle(title)
                .setContentText(message);
        Notification notify = builder.build();
        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //检查是否有权限推送通知给通知栏
        if(NotificationManagerCompat.from(this).areNotificationsEnabled()){
            //推送
            notifyMgr.notify(R.string.app_name,notify);
            Toast.makeText(this, "已推送消息到通知栏", Toast.LENGTH_SHORT).show();
        }else{
            //告知没有通知栏权限
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("通知栏权限")
                    .setMessage("尚未开启通知权限，请点击去进行开启")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                           /* intent.putExtra("app_uid", getApplicationInfo().uid);
                            Log.d(TAG, "onClick: app_package=" + getPackageName() + ",app_uid=" + getApplicationInfo().uid);*/
                            //运行的时候提示在已安装的应用中找不到该应用 ,传的数据信息不对
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //提示尚未开通权限
                            Toast.makeText(NotifyCounterActivity.this, "通知栏权限尚未开启，请先开启权限", Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            dialog.show();
        }
    }
}