package com.example.schedule.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.activity.ScheduleDetailActivity;
import com.example.schedule.bean.CalendarTransfer;
import com.example.schedule.bean.ScheduleArrange;
import com.example.schedule.calendar.Constant;
import com.example.schedule.calendar.LunarCalendar;
import com.example.schedule.util.DateUtil;

import java.util.ArrayList;

public class ScheduleListAdapter extends BaseAdapter implements OnItemClickListener {

    private static final String TAG = "ScheduleListAdapter";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final ArrayList<CalendarTransfer> mTranArray;
    private final ArrayList<ScheduleArrange> mArrangeList;

    public ScheduleListAdapter(Context context, ArrayList<CalendarTransfer> tranArray,
                               ArrayList<ScheduleArrange> arrangeList){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mTranArray = tranArray;
        this.mArrangeList = arrangeList;
    }
    @Override
    public int getCount() {
        return mTranArray.size();
    }

    @Override
    public Object getItem(int i) {
        return mTranArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_schedule,null);
            holder.week_number = (TextView) convertView.findViewById(R.id.week_number);
            holder.week_schedule = (TextView) convertView.findViewById(R.id.week_schedule);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.week_number.setText(Constant.weekArray[position]);
        if(position<5){
            holder.week_number.setTextColor(Color.BLACK);
        }else{
            holder.week_number.setTextColor(Color.RED);
        }
        //获取日历信息
        CalendarTransfer trans = mTranArray.get(position);
        String day = String.format("%s%02d%02d", trans.solar_year, trans.solar_month, trans.solar_day);
        String arrangeTitle = "";
        //获取对应日期的对应日程信息
        int i = 0;
        for (; i < mArrangeList.size(); i++) {
            if(mArrangeList.get(i).day.equals(day) == true){
                ScheduleArrange item = mArrangeList.get(i);
                arrangeTitle = String.format("%s时%s分：%s",item.hour,item.minute,item.title);
                break;
            }
        }
        //对应的日期没有日程信息
        if(i>=mArrangeList.size()){
            arrangeTitle="今日暂无日程安排";
        }
        String solar_date = String.format("%d月%d日", trans.solar_month, trans.solar_day);
        Log.d(TAG, "getView: trans.lunar_month - 1="+(trans.lunar_month - 1)); //哪里来的0
        String lunar_date = String.format("农历%s月%s", LunarCalendar.chineseNumber[trans.lunar_month - 1], LunarCalendar.getChinaDayString(trans.lunar_day));
        String holiday = "";
        if (DateUtil.checkHoliday(trans.day_name)==true){
            holiday = trans.day_name;
        }
        String content = String.format("%s %s %s\n%s",solar_date,lunar_date,holiday,arrangeTitle);
        Log.d(TAG, "getView: Schedule_content===="+content);
        holder.week_schedule.setText(content);
        String nowDate = DateUtil.getNowDate();
        if(nowDate.equals(day) == true){
            holder.week_schedule.setTextColor(Color.BLUE);
        }else{
            holder.week_schedule.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemClick i: "+i);
        CalendarTransfer trans = mTranArray.get(i);
        String day2 = String.format("%s%02d%02d", trans.solar_year, trans.solar_month, trans.solar_day);
        String solar_date2 = String.format("%s年%d月%d日", trans.solar_year, trans.solar_month, trans.solar_day);
        String lunar_date2 = String.format("农历%s月%s", LunarCalendar.chineseNumber[trans.lunar_month - 1], LunarCalendar.getChinaDayString(trans.lunar_day));
        String holiday2 = "";
        if(DateUtil.checkHoliday(trans.day_name) == true){
            holiday2 = trans.day_name;
        }
        Intent intent = new Intent(mContext, ScheduleDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("day",day2);
        bundle.putString("solar_date",solar_date2);
        bundle.putString("lunar_date",lunar_date2);
        bundle.putString("week",Constant.weekArray[i]);
        bundle.putString("holiday",holiday2);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    private final class ViewHolder {
        public TextView week_number;
        public TextView week_schedule;
    }
}
