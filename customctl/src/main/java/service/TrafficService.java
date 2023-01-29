package service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.customctl.MainApplication;
import com.example.customctl.MobileAssistantActivity;
import com.example.customctl.R;
import com.example.customctl.bean.AppInfo;
import com.example.customctl.bean.StringUtil;
import com.example.customctl.util.AppUtil;
import com.example.customctl.util.DateUtil;
import com.example.customctl.util.SharedUtil;

import java.util.ArrayList;

public class TrafficService extends Service {

    private static final String TAG = "TrafficService";
    private MainApplication app;
    private int limit_day;
    private int mNowDay;
    private Notification mNotify;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取当前应用的唯一实例
        app = MainApplication.getInstance();
        //从共享参数中获取日限额度数值
        limit_day = SharedUtil.getInstance(this).readInt("limit_day", 50);
        //立即启动流量刷新任务
        mHandler.post(mRefresh);
        return START_STICKY;
    }

    private Handler mHandler = new Handler();
    //定义一个流量刷新任务
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            //更新流量数据库
            refreshData();
            //刷新流量通知栏
            refreshNotify();
            //延迟10秒后再次启动流量刷新任务
            mHandler.postDelayed(this,10000);
        }
    };

    private void refreshNotify() {
        //计算今日的流量数据，根据限额和流量之前的大小关系显示不同颜色的进度条
        String lastDate = DateUtil.getAddDate("" + mNowDay, -1);
        //查询数据库获得截止到昨日的应用流量
        ArrayList<AppInfo> lastArray = app.mTrafficHelper.query("day=" + lastDate);
        //查询数据库获取截止到今日的应用流量
        ArrayList<AppInfo> thisArray = app.mTrafficHelper.query("day=" + mNowDay);
        //两者之差是今日的流量数据，所有应用的今日应用流量数据加起来
        long traffic_day = 0;
        for (int i = 0; i < thisArray.size(); i++) {
            AppInfo item = thisArray.get(i);
            for (int j = 0; j < lastArray.size(); j++) {
                if (item.uid == lastArray.get(j).uid){
                    item.traffic -= lastArray.get(j).traffic;
                    break;
                }
            }
            traffic_day += item.traffic;
        }
        String desc = "今日已用流量"+ StringUtil.formatData(traffic_day);
        int progress;
        int layoutId = R.layout.notify_traffic_green;
        float trafficM = traffic_day / 1024.0f / 1024.0f;
        if (trafficM>limit_day*2){
            //超出两倍限额，红色进度条
            progress = (int)((trafficM>limit_day*3)?100:(trafficM-limit_day*2)*100/limit_day);
            layoutId = R.layout.notify_traffic_red;
        }else if(trafficM>limit_day){
            //超出一倍限额，橙色进度条
            progress = (int)((trafficM>limit_day*2)?100:(trafficM-limit_day)*100/limit_day);
            layoutId = R.layout.notify_traffic_yellow;
        }else{
            progress = (int)(trafficM*100/limit_day);
        }
        Log.d(TAG, "refreshNotify: progress="+progress);
        //显示流量通知
        showFlowNotify(layoutId,desc,progress);

    }

    private void showFlowNotify(int layoutId, String desc, int progress) {
        //生成远程视图对象
        RemoteViews notify_traffic = new RemoteViews(this.getPackageName(), layoutId);
        //设置内部的文字描述
        notify_traffic.setTextViewText(R.id.tv_flow,desc);
        //设置内部进度条属性
        notify_traffic.setProgressBar(R.id.pb_flow,100,progress,false);
        //创建一个跳转到活动页面的意图
        Intent intent = new Intent(this, MobileAssistantActivity.class);
        //用于页面跳转的延迟意图
        PendingIntent clickIntent = PendingIntent.getActivity(this, R.string.app_name, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //通知消息构造器
        Notification.Builder builder = new Notification.Builder(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, getString(R.string.app_name));
        }
        builder.setContentIntent(clickIntent) // 点击意图
                .setContent(notify_traffic) //内容视图
                .setTicker("手机安全助手运行中")
                .setSmallIcon(R.drawable.ic_app);
        mNotify = builder.build();
        //将服务推送到前台的通知栏
        startForeground(9, mNotify);
    }

    private void refreshData() {
        mNowDay = Integer.parseInt(DateUtil.getNowDateTime("yyyyMMdd"));
        //获取最新的应用信息列表
        ArrayList<AppInfo> appInfoList = AppUtil.getAppInfo(this, 1);
        for (int i = 0; i < appInfoList.size(); i++) {
            AppInfo item = appInfoList.get(i);
            //Log.d(TAG, "before: traffic="+item.traffic+",uid="+item.uid);
            //获取该应用最新的流量接收数据
            //注意：如果你的设备如果不支持trafficstats.getuidrxbytes 这个方法的话，将会返回 UNSUPPORTED ，而 UNSUPPORTED 的值就是 -1
            // Android7之后，TrafficStats类的getUidRxBytes和getUidTxBytes只能查自身的流量。只有当前应用为系统应用之时，才能查其他应用的流量
            item.traffic = TrafficStats.getUidRxBytes(item.uid);
            /*item.traffic = TrafficStats.getUidRxBytes(item.uid)+ TrafficStats.getUidTxBytes(item.uid);
            if(item.traffic==0 || (TrafficStats.getUidRxBytes(item.uid)==-1) &&
                    (TrafficStats.getUidTxBytes(item.uid) == -1)){
                Toast.makeText(app, "不支持该方法获取流量", Toast.LENGTH_SHORT).show();
            }*/
            item.month = mNowDay/100;
            item.day = mNowDay;
           // Log.d(TAG, "refreshData: traffic="+item.traffic+",uid="+item.uid);
            appInfoList.set(i,item);
        }
        //往流量数据库插入最新应用流量记录
        app.mTrafficHelper.insert(appInfoList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNotify != null){
            stopForeground(true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
