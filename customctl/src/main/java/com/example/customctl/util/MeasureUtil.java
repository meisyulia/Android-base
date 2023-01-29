package com.example.customctl.util;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MeasureUtil {

    //文本尺寸测量
    public static float getTextWidth(String text,float textSize){
        if (text==null || text.length()<=0){
            return 0;
        }
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        return paint.measureText(text);
    }
    public static float getTextHeight(String text,float textSize){
        if (text==null || text.length()<=0){
            return 0;
        }
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文本自身的高度
        return fm.descent-fm.ascent;
        //文本所在的行高
        //return fm.bottom-fm.top+fm.leading
    }

    //布局尺寸测量
    public static float getRealHeight(Activity act,int resid){
        LinearLayout lLayout = (LinearLayout) act.findViewById(resid);
        return getRealHeight(lLayout);
    }
    public static float getRealHeight(View view,int resid){
        LinearLayout lLayout = (LinearLayout) view.findViewById(resid);
        return getRealHeight(lLayout);
    }
    //获取布局高度
    public static float getRealHeight(View child){
        LinearLayout lLayout = (LinearLayout) child;
        ViewGroup.LayoutParams params = lLayout.getLayoutParams();
        if(params == null){
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int heightSpec;
        if(params.height>0){
            heightSpec = View.MeasureSpec.makeMeasureSpec(params.height, View.MeasureSpec.EXACTLY);
        }else{
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        lLayout.measure(widthSpec,heightSpec);
        return lLayout.getMeasuredHeight(); //获取布局高度，获取布局宽度可调用lLayout.getMeasuredWidth
    }
}
