package com.example.customctl.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerTabStrip;

import com.example.customctl.R;

public class PagerTab extends PagerTabStrip {
    private static final String TAG = "PagerTab";
    private int textColor = Color.BLACK;
    private int textSize = 15;
    private int customBackground = -1;
    private int customOrientation = -1;
    private int customGravity = -1;

    public PagerTab(@NonNull Context context) {
        super(context);
    }

    public PagerTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if(attrs != null){
            TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.PagerTab);
            textColor = attrArray.getColor(R.styleable.PagerTab_textColor, textColor);
            textSize = attrArray.getDimensionPixelSize(R.styleable.PagerTab_textSize,textSize);
            customBackground=attrArray.getResourceId(R.styleable.PagerTab_customBackground,customBackground);
            customOrientation = attrArray.getInt(R.styleable.PagerTab_customOrientation,customOrientation);
            customGravity = attrArray.getInt(R.styleable.PagerTab_customGravity,customGravity);
            Log.d(TAG, "PagerTab: customBackground="+customBackground);
            Log.d(TAG, "PagerTab: customOrientation="+customOrientation);
            Log.d(TAG, "PagerTab: customGravity="+customGravity);
            attrArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setTextColor(textColor);
        setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
    }
}
