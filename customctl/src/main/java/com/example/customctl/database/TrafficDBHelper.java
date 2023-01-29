package com.example.customctl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.customctl.bean.AppInfo;

import java.util.ArrayList;

public class TrafficDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "traffic_db";
    private static final int DB_VERSION = 1;
    private static final String TAG = "TrafficDBHelper";
    private static final String TABLE_NAME = "traffic_info";
    private static TrafficDBHelper mHelper = null;
    private SQLiteDatabase mDB = null;


    public TrafficDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public TrafficDBHelper(@Nullable Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    //利用单例模式获取数据帮助器的唯一实例
    public static TrafficDBHelper getInstance(Context context,int version){
        if (version>0 && mHelper == null){
            mHelper = new TrafficDBHelper(context,version);
        }else if (mHelper == null){
            mHelper = new TrafficDBHelper(context);
        }
        return mHelper;
    }

    //打开数据库的读连接
    public SQLiteDatabase openReadLink(){
        if (mDB == null || !mDB.isOpen()){
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }
    //打开写连接
    public SQLiteDatabase openWriteLink(){
        if (mDB == null || !mDB.isOpen()){
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }

    //关闭连接
    public void closeLink(){
        if (mDB != null && mDB.isOpen()){
            mDB.close();
            mDB = null;
        }
    }

    //创建数据库，执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        String drop_sql = "DROP TABLE IF EXISTS "+TABLE_NAME+";";
        Log.d(TAG, "drop_sql: "+drop_sql);
        db.execSQL(drop_sql);
        String create_sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("
                +"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                +"month INTEGER NOT NULL,"+"day INTEGER NOT NULL,"
                +"uid INTEGER NOT NULL,"+"label VARCHAR NOT NULL,"
                +"package_name VARCHAR NOT NULL,"+"icon_path VARCHAR NOT NULL,"
                +"traffic LONG NOT NULL"
                +");";
        Log.d(TAG, "create_sql: "+create_sql);
        db.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //根据指定条件删除表记录
    public int delete(String condition){
        return mDB.delete(TABLE_NAME,condition,null);
    }
    //删除该表所有记录
    public int deleteAll(){
        return mDB.delete(TABLE_NAME,"1=1",null);
    }
    //往该表添加一条记录，返回值是行号
    public long insert(AppInfo info){
        ArrayList<AppInfo> infoArray = new ArrayList<>();
        infoArray.add(info);
        return insert(infoArray);
    }

    public long insert(ArrayList<AppInfo> infoArray) {
        long result = -1;
        for (AppInfo info : infoArray) {
            //如果存在相同的rowid记录，则更新记录
            if (info.rowid>0){
                String condition = String.format("rowid='%d'",info.rowid);
                update(info,condition);
                result = info.rowid;
                continue;
            }
            if(info.day>0 && info.uid>0){
                //如果存在相同日期的uid,则更新记录
                String condition = String.format("day='%d' and uid='%d'",info.day,info.uid);
                ArrayList<AppInfo> tempArray = new ArrayList<>();
                tempArray = query(condition);
                if (tempArray.size()>0){
                    update(info,condition);
                    result = tempArray.get(0).rowid;
                    continue;
                }
            }
            //否则，进行插入记录
            ContentValues cv = new ContentValues();
            cv.put("month",info.month);
            cv.put("day",info.day);
            cv.put("uid",info.uid);
            cv.put("label",info.label);
            cv.put("package_name",info.package_name);
            cv.put("icon_path",info.icon_path);
            cv.put("traffic",info.traffic);
            result = mDB.insert(TABLE_NAME,"",cv);
            if (result == -1){
                return result;
            }
        }
        return result;
    }

    public ArrayList<AppInfo> query(String condition) {
        String query_sql = String.format("select rowid,_id,month,day,uid,label,package_name,icon_path,traffic"+
                " from %s where %s;",TABLE_NAME,condition);
        /*Log.d(TAG, "query: query_sql="+query_sql);*/
        ArrayList<AppInfo> infoArray = new ArrayList<>();
        //游标查询
        Cursor cursor = mDB.rawQuery(query_sql, null);
        //循环取出游标指向的每条记录
        while (cursor.moveToNext()){
            AppInfo info = new AppInfo();
            info.rowid = cursor.getLong(0);
            info.xuhao = cursor.getInt(1);
            info.month = cursor.getInt(2);
            info.day = cursor.getInt(3);
            info.uid = cursor.getInt(4);
            info.label = cursor.getString(5);
            info.package_name = cursor.getString(6);
            info.icon_path = cursor.getString(7);
            info.traffic = cursor.getLong(8);
            infoArray.add(info);
        }
        cursor.close(); // 查询完毕记得关闭游标
        return infoArray;
    }

    //根据行号去查询指定记录
    public AppInfo queryById(long rowid){
        AppInfo info = new AppInfo();
        ArrayList<AppInfo> infoArray = query(String.format("rowid='%d'", rowid));
        if (infoArray.size()>0){
            info = infoArray.get(0);
        }
        return info;
    }

    public int update(AppInfo info, String condition) {
        ContentValues cv = new ContentValues();
        cv.put("month",info.month);
        cv.put("day",info.day);
        cv.put("uid",info.uid);
        cv.put("label",info.label);
        cv.put("package_name",info.package_name);
        cv.put("icon_path",info.icon_path);
        cv.put("traffic",info.traffic);
        //执行更新记录动作，该语句返回记录更新的数目
        return mDB.update(TABLE_NAME,cv,condition,null);
    }
    public int update(AppInfo info){
        return update(info,"rowid="+info.rowid);
    }
}
