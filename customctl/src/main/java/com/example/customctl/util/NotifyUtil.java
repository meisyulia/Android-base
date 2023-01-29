package com.example.customctl.util;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

public class NotifyUtil {
    @TargetApi(Build.VERSION_CODES.O)
    //创建通知渠道。Android 8.0 开始必须给每个通知分配对应的渠道
    public static void createNotifyChannel(Context ctx,String channelId){
        //创建一个默认重要性的通知渠道
        NotificationChannel channel = new NotificationChannel(channelId,
                "Channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setSound(null,null);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.setShowBadge(true);
        //从系统服务中获取通知管理器
        NotificationManager notifyMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        //创建指定的通知渠道
        notifyMgr.createNotificationChannel(channel);
    }
}
