package com.example.customctl;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customctl.util.MeasureUtil;

public class MeasureTextActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView tv_desc;
    private TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_text);
        Spinner sp_size = (Spinner) findViewById(R.id.sp_size);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_select, descArray);
        sp_size.setPrompt("请选择字体大小:");
        sp_size.setAdapter(adapter);
        sp_size.setSelection(0);
        sp_size.setOnItemSelectedListener(this);

        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_text = (TextView) findViewById(R.id.tv_text);
    }

    private String[] descArray={"12sp", "15sp", "17sp", "20sp", "22sp", "25sp", "27sp", "30sp"};
    private int[] sizeArray={12, 15, 17, 20, 22, 25, 27, 30};

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int textSize = sizeArray[i];
        tv_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        String text = tv_text.getText().toString();
        float textWidth = MeasureUtil.getTextWidth(text, textSize);
        float textHeight = MeasureUtil.getTextHeight(text, textSize);
        String desc = String.format("下面字体的宽度是%f,高度是%f", textWidth, textHeight);
        tv_desc.setText(desc);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}