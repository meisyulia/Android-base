package com.example.groupctl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class TabButtonActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox ck_select;
    private TextView tv_tab_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_button);
        ck_select = (CheckBox) findViewById(R.id.ck_select);
        ck_select.setOnCheckedChangeListener(this);
        tv_tab_button = (TextView) findViewById(R.id.tv_tab_button);
        tv_tab_button.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton.getId() == R.id.ck_select){
            tv_tab_button.setSelected(isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_tab_button){
            ck_select.setChecked(!ck_select.isChecked());
        }
    }
}