package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_icon;
    private Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_icon = (Button) findViewById(R.id.btn_icon);
        drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        //必须设置图片大小，否者不显示图片
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        findViewById(R.id.btn_top).setOnClickListener(this);
        findViewById(R.id.btn_bottom).setOnClickListener(this);
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btn_right).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_top)
        {
            btn_icon.setCompoundDrawables(null,drawable,null,null);
        }
        if(v.getId()==R.id.btn_bottom){
            btn_icon.setCompoundDrawables(null,null,null,drawable);
        }
        if(v.getId() == R.id.btn_left){
            btn_icon.setCompoundDrawables(drawable,null,null,null);
        }
        if(v.getId()==R.id.btn_right){
            btn_icon.setCompoundDrawables(null,null,drawable,null);
        }
    }
}