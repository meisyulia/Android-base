package com.example.schedule.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schedule.R;
import com.example.schedule.bean.ScheduleArrange;
import com.example.schedule.database.DbHelper;
import com.example.schedule.database.ScheduleArrangeHelper;

import java.util.Calendar;
import java.util.List;

public class ScheduleDetailActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "ScheduleDetailActivity";
    private Button btn_back;
    private Button btn_edit;
    private Button btn_save;
    private TextView tv_schedule_date;
    private TextView tv_schedule_time;
    private EditText et_schedule_title;
    private EditText et_schedule_content;
    private Spinner sp_schedule_alarm;
    private int alarmType=0;
    private String day;
    private String solar_date;
    private String lunar_date;
    private String month;
    private String week;
    private String holiday;
    private String detail_date;
    private ScheduleArrange arrange;
    private ScheduleArrangeHelper scheduleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_back.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        tv_schedule_date = (TextView) findViewById(R.id.tv_schedule_date);
        tv_schedule_time = (TextView) findViewById(R.id.tv_schedule_time);
        et_schedule_title = (EditText) findViewById(R.id.et_schedule_title);
        et_schedule_content = (EditText) findViewById(R.id.et_schedule_content);
        tv_schedule_time.setText("00:00");
        tv_schedule_time.setOnClickListener(this);

        //接收信息
        Bundle req = this.getIntent().getExtras();
        day = req.getString("day"); //eg:20230109
        solar_date = req.getString("solar_date");
        lunar_date = req.getString("lunar_date");
        month = day.substring(0, 6); //eg:202301
        week = req.getString("week");
        holiday = req.getString("holiday");
        detail_date = String.format("%s %s\n%s", solar_date, lunar_date, week);
        if(holiday!=null&&holiday.length()>0){
            detail_date = String.format("%s,今天是 %s",detail_date,holiday);
        }
        tv_schedule_date.setText(detail_date);
        Log.d(TAG, "onCreate: month="+month+",day="+day+",solar_date="+solar_date
                +",lunar_date="+lunar_date+",week="+week+",holiday="+holiday);

        sp_schedule_alarm = (Spinner) findViewById(R.id.sp_schedule_alarm);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_select, alarmArray);
        sp_schedule_alarm.setPrompt("请选择提醒间隔");
        sp_schedule_alarm.setAdapter(adapter);
        sp_schedule_alarm.setSelection(0);
        sp_schedule_alarm.setOnItemSelectedListener(new AlarmSelectedListener());
    }

    private String[] alarmArray= {"不提醒","提前5分钟","提前10分钟",
            "提前15分钟","提前半小时","提前1小时","当前时间后10秒"};
    private int[] advanceArray = {0,5,10,15,30,60,10};

    @Override
    protected void onResume() {
        super.onResume();
        arrange = new ScheduleArrange();
        //1.打开数据库
        scheduleHelper = new ScheduleArrangeHelper(this, DbHelper.db_name, null, 1);
        //2.查看数据库有没有对应day的日程安排
        List<ScheduleArrange> arrangeList = (List<ScheduleArrange>) scheduleHelper.queryInfoByDay(day);
        //3.如果有，则不编辑，进行展示
        //4.如果没有，则进行编辑日程安排
        if (arrangeList.size()>0){
            enableEdit(false);
            arrange = arrangeList.get(0);
            tv_schedule_time.setText(arrange.hour+":"+arrange.minute);
            sp_schedule_alarm.setSelection(arrange.alarm_type);
            et_schedule_title.setText(arrange.title);
            et_schedule_content.setText(arrange.content);
        }else{
            enableEdit(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scheduleHelper.close();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_schedule_time){
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog dialog = new TimePickerDialog(this, this,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            dialog.show();
        }
        else if(id == R.id.btn_back){
            finish();
        }else if(id == R.id.btn_edit){
            enableEdit(true);
        }else if(id == R.id.btn_save){

        }
    }

    private void enableEdit(boolean enabled) {
        tv_schedule_time.setEnabled(enabled);

        et_schedule_title.setEnabled(enabled);
        et_schedule_content.setEnabled(enabled);
        if(enabled){
            tv_schedule_time.setBackgroundResource(R.drawable.editext_selector);
            et_schedule_title.setBackgroundResource(R.drawable.editext_selector);
            et_schedule_content.setBackgroundResource(R.drawable.editext_selector);
        }else{
            tv_schedule_time.setBackgroundDrawable(null);
            et_schedule_title.setBackgroundDrawable(null);
            et_schedule_content.setBackgroundDrawable(null);
        }
        btn_edit.setVisibility(enabled?View.GONE:View.VISIBLE);
        btn_save.setVisibility(enabled?View.VISIBLE:View.GONE);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        String time = String.format("%02d:%02d", hourOfDay, minute);
        tv_schedule_time.setText(time);
    }

    private class AlarmSelectedListener implements OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            alarmType = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}