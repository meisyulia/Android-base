package com.example.customctl;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customctl.widget.OffsetLayout;

public class OnLayoutActivity extends AppCompatActivity {

    private OffsetLayout ol_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_layout);
        Spinner sp_offset = (Spinner) findViewById(R.id.sp_offset);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_select, descArray);
        sp_offset.setPrompt("请选择偏移大小");
        sp_offset.setAdapter(adapter);
        sp_offset.setSelection(0);
        sp_offset.setOnItemSelectedListener(new OffsetSelectedListener());
        ol_content = (OffsetLayout) findViewById(R.id.ol_content);
    }

    private String[] descArray = {"无偏移","向左偏移100","向右偏移100","向上偏移100","向下偏移100"};
    private int[] offsetArray = {0,-100,100,-100,100};

    private class OffsetSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            int offset = offsetArray[i];
            if(i==0||i==1||i==2){
                ol_content.setOffsetHorizontal(offset);
            }else if(i==3||i==4){
                ol_content.setOffsetVertical(offset);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}