package com.example.feifei.robot.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.feifei.robot.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feifei on 16-8-24.
 */
public class HistoryDao {
    private HistoryOpenHelper historyOpenHelper;
    private Log log;
    public HistoryDao(Context context){
        historyOpenHelper=new HistoryOpenHelper(context);
    }
    private static HistoryDao HistoryDao = null;
    //3,提供一个静态方法,如果当前类的对象为空,创建一个新的
    public static HistoryDao getInstance(Context context){
        if(HistoryDao == null){
            HistoryDao = new HistoryDao(context);
        }
        return HistoryDao;
    }

    /**增加一个条目
     *
     */
    public void insert(ChatMessage msg){
        //1,开启数据库,准备做写入操作
        SQLiteDatabase db = historyOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("date", msg.getDateStr());
        values.put("type", msg.getType());
        values.put("msg", msg.getMsg());
        db.insert("historymsg", null, values);

        db.close();
        log.i("db","插入聊天记录成功");
    }

    /**从数据库中删除一条电话号码
     *
     */
    public void delete(String phone){

    }

    /**
     * 根据电话号码去,更新拦截模式
     *
     */
    public void update(String phone,String mode){

    }

    /**
     * @return	查询到数据库中所有的号码以及拦截类型所在的集合
     */
    public List<ChatMessage> findAll(){
        SQLiteDatabase db = historyOpenHelper.getWritableDatabase();

        Cursor cursor = db.query("historymsg", new String[]{"date","type","msg"}, null, null, null, null, "_id asc");
        List<ChatMessage> HistoryList = new ArrayList<ChatMessage>();
        while(cursor.moveToNext()){
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setDate(cursor.getString(0));
            chatMessage.setType(cursor.getString(1));
            chatMessage.setMsg(cursor.getString(2));
            HistoryList.add(chatMessage);
        }
        cursor.close();
        db.close();
        log.i("db","读取聊天记录成功");
        return HistoryList;
    }

    /**
     * 每次查询20条数据
     * @param index	查询的索引值
     */
    public List<ChatMessage> find(int index,int count){
        SQLiteDatabase db = historyOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select date,type,msg from historymsg order by _id asc limit ?,?;", new String[]{index+"",count+""});

        List<ChatMessage> HistoryList = new ArrayList<ChatMessage>();
        while(cursor.moveToNext()){
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setDate(cursor.getString(0));
            chatMessage.setType(cursor.getString(1));
            chatMessage.setMsg(cursor.getString(2));
            HistoryList.add(chatMessage);
        }
        cursor.close();
        db.close();
        log.i("db","分页读取聊天记录成功");
        return HistoryList;
    }

    /**
     * @return	数据库中数据的总条目个数,返回0代表没有数据或异常
     */
    public int getCount(){
        SQLiteDatabase db = historyOpenHelper.getWritableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("select count(*) from historymsg;", null);
        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }



}
