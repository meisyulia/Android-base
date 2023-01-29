package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.myapplication.util.ViewUtil;

public class EditHideActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_hide;
    private EditText et_phone;
    private EditText et_password;
    private EditText et_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hide);

        ll_hide = (LinearLayout) findViewById(R.id.ll_hide);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        et_other = (EditText) findViewById(R.id.et_other);

        ll_hide.setOnClickListener(this);
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone));
        et_password.addTextChangedListener(new HideTextWatcher(et_password));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ll_hide){
            //实际上不只是et_other的软键盘会关闭，其他编辑框的软键盘也会关闭
            //因为方法内部去获取视图的WindowToken,这个Token在每个页面上都是唯一的
            ViewUtil.hideOneInputMethod(EditHideActivity.this,et_other);
        }
    }

    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;
        private CharSequence mStr;
        public HideTextWatcher(EditText v) {
            super();
            mView = v;
            //获取最大长度
            mMaxLength= ViewUtil.getMaxLength(v);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mStr = charSequence;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(mStr == null || mStr.length() == 0)
                return;
            if(mStr.length() == 11 && mMaxLength == 11){
                ViewUtil.hideAllInputMethod(EditHideActivity.this);
            }
            if(mStr.length() == 6 && mMaxLength == 6){
                ViewUtil.hideOneInputMethod(EditHideActivity.this,mView);
            }
        }
    }
}