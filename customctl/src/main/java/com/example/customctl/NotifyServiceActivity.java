package com.example.customctl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import service.MusicService;

public class NotifyServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_song;
    private boolean isPlaying=true;
    private Button btn_send_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_service);
        et_song = (EditText) findViewById(R.id.et_song);
        btn_send_service = (Button) findViewById(R.id.btn_send_service);
        btn_send_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_send_service){
            //创建一个通往音乐服务的意图
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("is_play",isPlaying);
            intent.putExtra("song",et_song.getText().toString());
            if (isPlaying){
                //启动音乐服务
                startService(intent);
                btn_send_service.setText("停止播放音乐");
            }else{
                stopService(intent);
                btn_send_service.setText("开始播放音乐");
            }
            isPlaying = !isPlaying;
        }
    }
}