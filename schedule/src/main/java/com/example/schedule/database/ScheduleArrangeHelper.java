package com.example.schedule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.schedule.bean.ScheduleArrange;

import java.util.ArrayList;
import java.util.List;

public class ScheduleArrangeHelper extends DbHelper{
    public ScheduleArrangeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mTableName = "ScheduleArrange";
        mSelectSQL = String.format("select _id,month,day,hour,minute,title,content,update_time,alarm_type from %s where "
        ,mTableName);
    }

    //添加日程安排
    public boolean add(ScheduleArrange data){
        ArrayList<ScheduleArrange> data_list = new ArrayList<>();
        data_list.add(data);
        return add(data_list);
    }

    public boolean add(ArrayList<ScheduleArrange> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            ScheduleArrange data = dataList.get(i);
            //ContentValues对象
            ContentValues cv = new ContentValues();
            cv.put("month",data.month);
            cv.put("day",data.day);
            cv.put("hour",data.hour);
            cv.put("minute",data.minute);
            cv.put("title",data.title);
            cv.put("content",data.content);
            cv.put("update_time",data.update_time);
            cv.put("alarm_type",data.alarm_type);

            Log.d(TAG, "add: cv.toString():"+cv.toString());
            //如果数据库有对应的day的日程安排则更新否则插入新数据
            if(getCount(data.day)<=0){
                //数据库无记录则则插入
                long result = mWriteDB.insert(mTableName, "", cv);
                //添加成功后返回行号，失败则返回-1
                if(result == -1){
                    return false;
                }
            }else{
                update(data);
            }
        }
        return true;
    }

    private void update(ScheduleArrange data) {
        String update_sql = String.format("update %s set month='%s',day='%s',hour='%s',minute='%s',title='%s',content='%s',update_time='%s',alarm_type='%d' where ",
                mTableName, data.month, data.day, data.hour, data.minute, data.title, data.content, data.update_time, data.alarm_type);
        //如果有序号，用序号更新，没有则根据day(日期）更新
        if(data.xuhao>0){
            update_sql = String.format("%s _id='%d';",update_sql,data.xuhao);
        }else{
            update_sql = String.format("%s day='%s';",update_sql,data.day);
        }
        Log.d(TAG, "update_sql: "+update_sql);
        mWriteDB.execSQL(update_sql);

    }

    protected List<?> queryInfo(String sql){
        Log.d(TAG, "queryInfo sql= "+sql);
        ArrayList<ScheduleArrange> data_list = new ArrayList<>();
        Cursor cursor = mReadDB.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            for (;;cursor.moveToNext()){
                ScheduleArrange data_info = new ScheduleArrange();
                data_info.xuhao = cursor.getInt(0);
                data_info.month = cursor.getString(1);
                data_info.day = cursor.getString(2);
                data_info.hour = cursor.getString(3);
                data_info.minute = cursor.getString(4);
                data_info.title = cursor.getString(5);
                data_info.content = cursor.getString(6);
                data_info.update_time = cursor.getString(7);
                data_info.alarm_type = cursor.getInt(8);

                data_list.add(data_info);
                if(cursor.isLast() == true){
                    break;
                }
            }
        }
        cursor.close();
        return data_list;
    }

    private int getCount(String day) {
        String sql = String.format("%s day='%s';", mSelectSQL, day);
        return(queryCount(sql));
    }

    public List<?> queryInfoByDay(String day){
        String sql = String.format("%s day='%s';",mSelectSQL,day);
        return queryInfo(sql);
    }

    public List<?> queryInfoByMonth(String month){
        String sql = String.format("%s month='%s';",mSelectSQL,month);
        return queryInfo(sql);
    }

    public List<?> queryInfoByDayRange(String begin_day,String end_day){
        String sql = String.format("%s day>='%s' and day<='%s';",mSelectSQL,begin_day,end_day);
        return queryInfo(sql);
    }
}
