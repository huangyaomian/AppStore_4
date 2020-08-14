package com.hym.appstore.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static SQLiteDatabase database;

    public static void initDB(Context context){
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        database = dbOpenHelper.getWritableDatabase();
    }

    //獲取數據庫當中全部行的内容，存儲到集合中
    public static List<String> getAllSearchHistoryList(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.query("searchHistory", null, null, null, null, null, "id desc");
        while (cursor.moveToNext()) {
            String keyword = cursor.getString(cursor.getColumnIndex("keyword"));
            list.add(keyword);
        }
        return list;
    }

    //修改數據庫當中的行信息當中的選中記錄
    public static void updateSearchHistory(String keyword){
            ContentValues values = new ContentValues();
            values.put("keyword",keyword);
            database.update("searchHistory",values,"keyword=?",new String[]{keyword});
    }

    //向数据库中插入一条记录
    public static void insertSearchHistory(String keyword){
        ContentValues values = new ContentValues();
        values.put("keyword",keyword);
        database.insert("searchHistory",null,values);
    }

    //清空数据库中搜索历史记录表中的数据
    public static void DeleteAllSearchHistory(){
        database.execSQL("delete from " + "searchHistory");
    }
}
