package com.example.customctl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class NotifyProgressActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar pb_progress;
    private EditText et_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_progress);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        et_progress = (EditText) findViewById(R.id.et_progress);
        findViewById(R.id.btn_progress).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_progress){
            int progress = Integer.parseInt(et_progress.getText().toString());
            pb_progress.setProgress(progress);
        }
    }
}