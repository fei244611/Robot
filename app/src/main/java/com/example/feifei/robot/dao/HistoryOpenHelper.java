package com.example.feifei.robot.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by feifei on 16-8-24.
 */
public class HistoryOpenHelper extends SQLiteOpenHelper{
    private Log log;
    public HistoryOpenHelper(Context context){
        super(context, "historymsg.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table historymsg " +
                "(_id integer primary key autoincrement , date varchar(20) , type varchar(10), msg varchar(255));");
        log.i("db","创建数据库成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
