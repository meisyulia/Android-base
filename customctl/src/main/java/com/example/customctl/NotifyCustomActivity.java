package com.example.customctl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

public class NotifyCustomActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_song;
    private String PAUSE_EVENT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_custom);
        et_song = (EditText) findViewById(R.id.et_song);
        findViewById(R.id.btn_send_custom).setOnClickListener(this);
        //获取暂停时间的标识串
        PAUSE_EVENT = getResources().getString(R.string.pause_event);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send_custom){
            Notification notify = getNotify(this, PAUSE_EVENT, et_song.getText().toString(),
                    true, 50, SystemClock.elapsedRealtime());
            NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.notify(R.string.app_name,notify);
        }
    }

    private Notification getNotify(Context ctx,String event,String song,boolean isPlaying,int progress,long time){
        Intent intent = new Intent(event);
        PendingIntent broadIntent = PendingIntent.getBroadcast(ctx, R.string.app_name,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //生成远程视图对象
        RemoteViews notify_music = new RemoteViews(ctx.getPackageName(), R.layout.notify_music);
        if(isPlaying){
            notify_music.setTextViewText(R.id.btn_play,"暂停");
            notify_music.setTextViewText(R.id.tv_play,song+"正在播放");
            notify_music.setChronometer(R.id.chr_play,time,"%s",true);
        }else{
            notify_music.setTextViewText(R.id.btn_play,"继续");
            notify_music.setTextViewText(R.id.tv_play,song+"暂停播放");
            notify_music.setChronometer(R.id.chr_play,time,"%s",false);
        }
        //设置远程视图内部的进度条属性
        notify_music.setProgressBar(R.id.pb_play,100,progress,false);
        //整个通知已有点击意图，那如何给单个控件添加点击事件？
        //办法：设置控件点击事件的广播意图，一旦点击该控件，就发出对应事件的广播
        notify_music.setOnClickPendingIntent(R.id.btn_play,broadIntent);
        //用于整个通知被点击的跳转意图
        Intent intent1 = new Intent(ctx, NotifyCustomActivity.class);
        PendingIntent clickIntent = PendingIntent.getActivity(ctx, R.string.app_name, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(ctx);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(ctx, getString(R.string.app_name));
        }
        builder.setContentIntent(clickIntent)
                .setContent(notify_music)
                .setTicker(song)
                .setSmallIcon(R.drawable.tt_s);
        Notification notify = builder.build();
        return notify;
    }
}