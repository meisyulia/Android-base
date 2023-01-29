package com.example.customctl;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customctl.widget.CircleAnimation;

public class CircleAnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_animation);
        Button btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
        LinearLayout ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        animation = new CircleAnimation(this);
        ll_layout.addView(animation);
        animation.render();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play){
            animation.play();
        }
    }
}