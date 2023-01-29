package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class EditAutoActivity extends AppCompatActivity {

    private AutoCompleteTextView ac_text;
    private String[] hintArray= {"第一次","第一次写代码","第一次领工资","第二","第二个"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_auto);

        ac_text = (AutoCompleteTextView) findViewById(R.id.ac_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_dropdown, hintArray);
        ac_text.setAdapter(adapter);
    }
}