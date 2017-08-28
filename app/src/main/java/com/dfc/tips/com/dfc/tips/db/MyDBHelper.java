package com.dfc.tips.com.dfc.tips.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "TestSQLite";
    public static final int VERSION = 1;

    //必须要有构造函数
    public MyDBHelper(Context context, String name, CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
    }

    // 当第一次创建数据库的时候，调用该方法
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table if not exists tips_table(name varchar(20),content varchar,type varchar,createtime varchar,image varcher,username varcher)";
        String sql2 = "create table if not exists tips_basket(name varchar(20),content varchar,type varchar,createtime varchar,image varcher,username varcher)";
        String sql3 = "create table if not exists wordnote_word(english varchar(20),chinese varchar,message varchar,username varcher)";
        String sql4 = "create table if not exists wordnote_sentence(english varchar(20),chinese varchar,message varchar,username varcher)";
        String sql5 = "create table if not exists accountbook(time varchar(20),amount float,type varchar(20),message varcher,username varcher)";
        String sql11 = "create table if not exists user(username varchar(20),nickname varchar(20),gender int,year int,month int,day int,money float,function varcher,head varcher)";
        String sql6 = "drop table tips_table";
        String sql7 = "drop table tips_basket";
        String sql8 = "drop table wordnote_word";
        String sql9 = "drop table wordnote_sentence";
        String sql10 = "drop table accountbook";
        String sql12 = "drop table user";
//输出创建数据库的日志信息
        Log.i(TAG, "create Database------------->");
//execSQL函数用于执行SQL语句
//        db.execSQL(sql6);
//        db.execSQL(sql7);
//        db.execSQL(sql8);
//        db.execSQL(sql9);
//        db.execSQL(sql10);
//        db.execSQL(sql12);
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql11);

    }

    //当更新数据库的时候执行该方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//输出更新数据库的日志信息
        Log.i(TAG, "update Database------------->");
    }
}
