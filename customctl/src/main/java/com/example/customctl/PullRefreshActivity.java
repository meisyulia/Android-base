package com.example.customctl;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customctl.util.MeasureUtil;

public class PullRefreshActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_header;
    private Button btn_pull;
    private int mHeight;
    private boolean bStart = false;
    private int mOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh);
        ll_header = (LinearLayout) findViewById(R.id.ll_header);
        ll_header.setVisibility(View.GONE);
        btn_pull = (Button) findViewById(R.id.btn_pull);
        btn_pull.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //计算头部高度
        mHeight = (int) MeasureUtil.getRealHeight(ll_header);
        if (v.getId() == R.id.btn_pull){
            if(bStart == false){
                mOffset = -mHeight;
                btn_pull.setEnabled(false);
                mHandler.post(mRefresh);
            }else{
                btn_pull.setText("开始刷新");
                ll_header.setVisibility(View.GONE);
            }
            bStart = !bStart;
        }
    }

    private Handler mHandler = new Handler();
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            if(mOffset<=0){
                ll_header.setPadding(0,mOffset,0,0);
                ll_header.setVisibility(View.VISIBLE);
                mOffset +=8;
                mHandler.postDelayed(mRefresh,80);
            }else{
                btn_pull.setText("恢复页面");
                btn_pull.setEnabled(true);
            }
        }
    };
}