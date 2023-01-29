package service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.example.customctl.NotifyServiceActivity;
import com.example.customctl.R;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private IBinder mBinder = new LocalBinder();
    private long mBaseTime;
    private boolean isPlaying=true;
    private String mSong;
    private long mPauseTime=0;
    private PauseReceiver pauseReceiver;

    //定义一个当前服务的粘合剂，用于将该服务粘到活动页面的进程中
    public class LocalBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private Handler mHandler = new Handler();
    private int mProgress = 0;
    private String PAUSE_EVENT="";
    private Runnable mPlay = new Runnable() {
        @Override
        public void run() {
            if (isPlaying)
            {
                //正在播放需要刷新播放进度
                if(mProgress<100){
                    mProgress +=2;
                }else{
                    mProgress = 0;
                }
                mHandler.postDelayed(mPlay,1000);
            }
            //获取自定义消息的通知对象
            Notification notify = getNotify(MusicService.this, PAUSE_EVENT, mSong, isPlaying, mProgress, mBaseTime);
            //将服务推送到前台的通知栏
            startForeground(2,notify);
        }
    };


    //自定义消息的通知对象
    private Notification getNotify(Context ctx, String event, String song, boolean isPlaying, int progress, long time) {
        // 创建一个广播事件的意图
        Intent intent1 = new Intent(event);
        // 创建一个用于广播的延迟意图
        PendingIntent broadIntent = PendingIntent.getBroadcast(
                ctx, R.string.app_name, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        // 根据布局文件notify_music.xml生成远程视图对象
        RemoteViews notify_music = new RemoteViews(ctx.getPackageName(), R.layout.notify_music);
        if (isPlaying) { // 正在播放
            notify_music.setTextViewText(R.id.btn_play, "暂停"); // 设置按钮文字
            notify_music.setTextViewText(R.id.tv_play, song + "正在播放"); // 设置文本文字
            notify_music.setChronometer(R.id.chr_play, time, "%s", true); // 设置计数器
        } else { // 不在播放
            notify_music.setTextViewText(R.id.btn_play, "继续"); // 设置按钮文字
            notify_music.setTextViewText(R.id.tv_play, song + "暂停播放"); // 设置文本文字
            notify_music.setChronometer(R.id.chr_play, time, "%s", false); // 设置计数器
        }
        // 设置远程视图内部的进度条属性
        notify_music.setProgressBar(R.id.pb_play, 100, progress, false);
        // 整个通知已经有点击意图了，那要如何给单个控件添加点击事件？
        // 办法是设置控件点击的广播意图，一旦点击该控件，就发出对应事件的广播。
        notify_music.setOnClickPendingIntent(R.id.btn_play, broadIntent);
        // 创建一个跳转到活动页面的意图
        Intent intent2 = new Intent(ctx, NotifyServiceActivity.class);
        // 创建一个用于页面跳转的延迟意图
        PendingIntent clickIntent = PendingIntent.getActivity(ctx,
                R.string.app_name, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        // 创建一个通知消息的构造器
        Notification.Builder builder = new Notification.Builder(ctx);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0开始必须给每个通知分配对应的渠道
            builder = new Notification.Builder(ctx, getString(R.string.app_name));
        }
        builder.setContentIntent(clickIntent) // 设置内容的点击意图
                .setContent(notify_music) // 设置内容视图
                .setTicker(song) // 设置状态栏里面的提示文本
                .setSmallIcon(R.drawable.tt_s); // 设置状态栏里的小图标
        // 根据消息构造器构建一个通知对象
        Notification notify = builder.build();
        return notify;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取从设备重启后经历的时间值
        mBaseTime = SystemClock.elapsedRealtime();
        //从意图中获取是否正在播放和歌曲名称
        isPlaying = intent.getBooleanExtra("is_play", true);
        mSong = intent.getStringExtra("song");
        Log.d(TAG, "onStartCommand: isPlating="+isPlaying+", mSong="+mSong);
        //延迟200毫秒后启动音乐播放任务
        mHandler.postDelayed(mPlay,200);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PAUSE_EVENT = getResources().getString(R.string.app_name);
        //注册光标接收器，注册之后，才能正常接收广播
        pauseReceiver = new PauseReceiver();
        registerReceiver(pauseReceiver,new IntentFilter(PAUSE_EVENT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pauseReceiver);
    }

    private class PauseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                isPlaying= !isPlaying;
                if(isPlaying){
                    //需要进行播放
                    mHandler.postDelayed(mPlay,200);
                    if (mPauseTime>0){
                        long gap = SystemClock.elapsedRealtime() - mPauseTime;
                        mBaseTime += gap;
                    }
                }else{
                    //需要暂停播放
                    mPauseTime=SystemClock.elapsedRealtime();
                }
            }
        }
    }
}
