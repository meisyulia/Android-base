package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.util.DateUtil;

public class ActJumpActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ActJumpActivity";
    private String mStr = "";
    private TextView tv_life;

    private void refreshLife(String desc){
        Log.d(TAG, desc);
        mStr = String.format("%s%s %s %s\n",mStr, DateUtil.getNowTimeDetail(),TAG,desc);
        tv_life.setText(mStr);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_jump);
        findViewById(R.id.btn_act_next).setOnClickListener(this);
        tv_life = (TextView) findViewById(R.id.tv_life);
        refreshLife("onCreate");
    }

    @Override
    protected void onStart() {
        refreshLife("onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        refreshLife("onStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        refreshLife("onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        refreshLife("onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        refreshLife("onRestart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        refreshLife("onDestroy");
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_act_next){
            Intent intent = new Intent(this, ActNextActivity.class);
            startActivityForResult(intent,0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        String nextLife = data.getStringExtra("life");
        refreshLife("\n" + nextLife);
        refreshLife("onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

    }


}