package com.example.customctl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.customctl.widget.BeforeRelativeLayout;

public class OnDrawActivity extends AppCompatActivity {

    private BeforeRelativeLayout brl_content;
    private Button btn_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_draw);
        brl_content = (BeforeRelativeLayout) findViewById(R.id.brl_content);
        btn_center = (Button) findViewById(R.id.btn_center);

        ArrayAdapter<String> drawAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, descArray);
        Spinner sp_draw = (Spinner) findViewById(R.id.sp_draw);
        sp_draw.setPrompt("请选择绘图方式");
        sp_draw.setAdapter(drawAdapter);
        sp_draw.setOnItemSelectedListener(new DrawSelectedListener());
        sp_draw.setSelection(0);
    }
    private String[] descArray={"不画图", "画矩形", "画圆角矩形", "画圆圈", "画椭圆", "画叉叉"};
    private int[] typeArray={0, 1, 2, 3, 4, 5};

    private class DrawSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            int type = typeArray[i];
            if(type == 5){
                btn_center.setVisibility(View.VISIBLE);
            }else{
                btn_center.setVisibility(View.GONE);
            }
            brl_content.setDrawType(type);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}