package com.example.customctl.util;

import android.content.Context;

public class Utils {
    //根据手机的分辨率从dp的单位转成为px(像素)
    public static int dip2px(Context context,float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale+0.5f);
    }
    //根据手机的分辨率从px单位转换成dp
    public static int px2dip(Context context,float pxValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue/scale+0.5f);
    }
}
