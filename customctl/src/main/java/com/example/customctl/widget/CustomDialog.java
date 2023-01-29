package com.example.customctl.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.customctl.R;

public class CustomDialog implements View.OnClickListener, DatePicker.OnDateChangedListener {

    private final View view;
    private final Dialog dialog;
    private final TextView tv_title;
    private final DatePicker dp_date;
    private final Button btn_ok;
    private OnDateSetListener mDateSetListener;

    public CustomDialog(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_date, null);
        //修改对话框样式
        dialog = new Dialog(context, R.style.CustomDateDialog);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        dp_date = (DatePicker) view.findViewById(R.id.dp_date);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }
    //设置日期
    public void setDate(int year, int month, int day, OnDateSetListener listener){
        dp_date.init(year,month,day,this);
        mDateSetListener = listener;
    }
    //显示对话框
    public void show(){
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    //关闭对话框
    public void dismiss(){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public boolean isShowing(){
        if(dialog != null){
            return dialog.isShowing();
        }else{
            return false;
        }
    }
    @Override
    public void onClick(View v) {
        dismiss();
        if (mDateSetListener != null){
            dp_date.clearFocus();
            mDateSetListener.onDateSet(dp_date.getYear(),
                    dp_date.getMonth()+1,dp_date.getDayOfMonth());
        }
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        dp_date.init(year,monthOfYear,dayOfMonth,this);
    }

    public interface OnDateSetListener{
        void onDateSet(int year,int monthOfYear,int dayOfMonth);
    }
}
