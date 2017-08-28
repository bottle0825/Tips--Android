package com.dfc.tips.com.dfc.tips.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dfc.tips.R;
import com.dfc.tips.com.dfc.tips.msg.Account;
import com.dfc.tips.com.dfc.tips.msg.Global_variable;
import com.dfc.tips.com.dfc.tips.msg.Sentences;
import com.dfc.tips.com.dfc.tips.msg.Tips;
import com.dfc.tips.com.dfc.tips.msg.User;
import com.dfc.tips.com.dfc.tips.msg.Words;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.accounts;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.sentences;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.tips_basket;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.tips_note;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Luser;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.words;

/**
 * Created by dfc on 2017/7/13.
 */

public class MyDB {

    Global_variable mGlobal_variable;
    /*
   SQLite相关操作
    */
    //创建数据库
    public void createDB(Context context){
        //创建StuDBHelper对象
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
    }
    //更新数据库
    public void updateDB(Context context){
        // 数据库版本的更新,由原来的1变为2
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,2);
        SQLiteDatabase db =dbHelper.getReadableDatabase();
    }
    //插入数据的方法
    public void insertuser(Context context,User user){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        cv.put("username", user.getUsername());
        cv.put("nickname", user.getNickname());
        cv.put("gender", user.getGender());
        cv.put("year", user.getBirth_year());
        cv.put("month",user.getBirth_month() );
        cv.put("day",user.getBirth_day());
        cv.put("money",user.getMoney());
        cv.put("function",user.getFunction());
        cv.put("head",user.getHead());
        //调用insert方法，将数据插入数据库
        db.insert("user", null, cv);
        System.out.println("+++++++++++++++++++++++--- insert ---++++++++++++++++++++++++++++++++");
        //关闭数据库
        db.close();
    }
    public void readuser(Context context,String username){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        Cursor cursor = db.query("user",null, null, null, null, null, null);
        //判断游标是否为空
        if(cursor.moveToFirst()) {
            //遍历游标
            do  {
                System.out.println("+++++++++++++++++++++++--- "+cursor.getString(0) +"---++++++++++++++++++++++++++++++++");
                if (cursor.getString(0).equals(username)){
                    Luser.setUsername(cursor.getString(0));
                    Luser.setNickname(cursor.getString(1));
                    Luser.setGender(cursor.getString(2));
                    Luser.setBirth_year(cursor.getString(3));
                    Luser.setBirth_month(cursor.getString(4));
                    Luser.setBirth_day(cursor.getString(5));
                    Luser.setMoney(cursor.getFloat(6));
                    Luser.setFunction(cursor.getString(7));
                    Luser.setHead(cursor.getString(8));
                }
            }while (cursor.moveToNext());
        }
        //关闭数据库
        db.close();
    }
    public boolean queryuser(Context context,String username){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        //参数1：表名
        //参数2：要想显示的列
        //参数3：where子句
        //参数4：where子句对应的条件值
        //参数5：分组方式
        //参数6：having条件
        //参数7：排序方式
        Cursor cursor = db.query("user",new String[]{"username"}, "username=?", new String[]{username}, null, null, null);
        //判断游标是否为空
        int i = 0;
        boolean qflag = true;
        while(cursor.moveToNext()){
            String qname = cursor.getString(0);
            if (qname!=null){
                {
                    qflag = false;
                    Log.e("note",qname+"--------------------------------------------------------");
                }
                break;
            }
        }
        //关闭数据库
        db.close();
        return qflag;
    }
    public void deleteuser(Context context,String username){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        System.out.println("+++++++++++++++++++++++--- delete ---++++++++++++++++++++++++++++++++");
        String whereClauses = null;
        whereClauses = "username=?";
        String [] whereArgs = {username};
        //调用delete方法，删除数据
        db.delete("user", whereClauses, whereArgs);
    }
    public void insert(Context context,String table,Tips tips){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        cv.put("name", tips.getTips_name());
        cv.put("content", tips.getTips_content());
        cv.put("type", tips.getTips_type());
        cv.put("createtime", tips.getTips_createtime());
        cv.put("image",tips.getTips_img() );
        cv.put("username",Login_username);
        //调用insert方法，将数据插入数据库
        db.insert(table, null, cv);
        //关闭数据库
        db.close();
    }

    public void insertword(Context context,Words words){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        cv.put("english", words.getEnglish());
        cv.put("chinese", words.getChinese());
        cv.put("message", words.getMessage());
        cv.put("username", words.getUsername());
        //调用insert方法，将数据插入数据库
        db.insert("wordnote_word", null, cv);
        //关闭数据库
        db.close();
    }
    public void insertsentence(Context context, Sentences sentence){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        cv.put("english", sentence.getEnglish());
        cv.put("chinese", sentence.getChinese());
        cv.put("message", sentence.getMessage());
        cv.put("username", sentence.getUsername());
        //调用insert方法，将数据插入数据库
        db.insert("wordnote_sentence", null, cv);
        //关闭数据库
        db.close();
    }
    public void insertaccount(Context context, Account account){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        System.out.println("+++++++++++++++++++++++--- insert ---++++++++++++++++++++++++++++++++"+account);
        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        cv.put("time", account.getTime());
        cv.put("amount", account.getAmount());
        cv.put("type", account.getType());
        cv.put("message", account.getMessage());
        cv.put("username", account.getUsername());
        //调用insert方法，将数据插入数据库
        db.insert("accountbook", null, cv);
        //关闭数据库
        db.close();
    }
    public void readDB(Context context,String table,String username){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        //参数1：表名
        //参数2：要想显示的列
        //参数3：where子句
        //参数4：where子句对应的条件值
        //参数5：分组方式
        //参数6：having条件
        //参数7：排序方式
        Cursor cursor = db.query(table,null, null, null, null, null, null);
        //判断游标是否为空
        if (table=="tips_table"){
            if(cursor.moveToFirst()) {
                //遍历游标
                do  {
                    if (username.equals(cursor.getString(5))){
                        Tips tips = new Tips();
                        tips.setTips_name(cursor.getString(0));
                        tips.setTips_content(cursor.getString(1));
                        tips.setTips_type(cursor.getString(2));
                        tips.setTips_createtime(cursor.getString(3));
                        tips.setTips_img(cursor.getString(4));
                        tips.setTips_username(cursor.getString(5));
                        tips_note.add(tips);
                    }
                }while (cursor.moveToNext());
            }
        }if (table=="tips_basket"){
            if(cursor.moveToFirst()) {
                //遍历游标
                do {
                    if (username.equals(cursor.getString(5))){
                        Tips tips = new Tips();
                        //获得tips标题
                        tips.setTips_name(cursor.getString(0));
                        //获得tips内容
                        tips.setTips_content(cursor.getString(1));
                        //获得tips种类
                        tips.setTips_type(cursor.getString(2));
                        //获得tips种类
                        tips.setTips_createtime(cursor.getString(3));
                        //获得tips种类
                        tips.setTips_img(cursor.getString(4));
                        tips.setTips_username(cursor.getString(5));
//                tipss.add(tips);
                        tips_basket.add(tips);
                    }
                }while (cursor.moveToNext());
            }
        }if (table=="wordnote_word") {
            if (cursor.moveToFirst()) {
                //遍历游标
                do {
                    if (username.equals(cursor.getString(3))){
                        Words word = new Words();
                        word.setEnglish(cursor.getString(0));
                        word.setChinese(cursor.getString(1));
                        word.setMessage(cursor.getString(2));
                        word.setUsername(cursor.getString(3));
                        words.add(word);
                    }
                }while (cursor.moveToNext());
            }
        }if (table=="wordnote_sentence") {
            if (cursor.moveToFirst()) {
                //遍历游标
                do {
                    if (username.equals(cursor.getString(3))){
                        Sentences sentence = new Sentences();
                        sentence.setEnglish(cursor.getString(0));
                        sentence.setChinese(cursor.getString(1));
                        sentence.setMessage(cursor.getString(2));
                        sentence.setUsername(cursor.getString(3));
                        sentences.add(sentence);
                    }
                    } while (cursor.moveToNext());
                }
            }if (table=="accountbook") {
            if (cursor.moveToFirst()) {
                System.out.println("+++++++++++++++++++++++--- read ---++++++++++++++++++++++++++++++++"+cursor);
                //遍历游标
                do {
                    if (username.equals(cursor.getString(4))){
                        Account account = new Account();
                        account.setTime(cursor.getString(0));
                        account.setAmount(cursor.getFloat(1));
                        account.setType(cursor.getString(2));
                        account.setMessage(cursor.getString(3));
                        account.setUsername(cursor.getString(4));
                        accounts.add(account);
                    }
                }while (cursor.moveToNext());
            }
        }
        //关闭数据库
        db.close();
    }
    public boolean queryDB(Context context,String table,String name,String username){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        //参数1：表名
        //参数2：要想显示的列
        //参数3：where子句
        //参数4：where子句对应的条件值
        //参数5：分组方式
        //参数6：having条件
        //参数7：排序方式
        Cursor cursor = db.query(table,new String[]{"name,username"}, "name=?", new String[]{name}, null, null, null);
        //判断游标是否为空
        int i = 0;
        boolean qflag = true;
        while(cursor.moveToNext()){
            String qname = cursor.getString(0);
            String qusername = cursor.getString(1);
            if (qname!=null){
                {
                    if (qusername.equals(username)){
                        qflag = false;
                    }
                }
                break;
            }
        }
        //关闭数据库
        db.close();
        return qflag;
    }

    //修改数据的方法
//    public void modifyDB(Context context,String name, String content, String type, String createtime){
//        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
//        //得到一个可写的数据库
//        SQLiteDatabase db =dbHelper.getWritableDatabase();
//        dbHelper.onCreate(db);
//        ContentValues cv = new ContentValues();
//        cv.put("name", name);
//        cv.put("content", content);
//        cv.put("type", type);
//        cv.put("createtime", createtime);
//        //where 子句 "?"是占位符号，对应后面的"1",
//        String whereClause="name="+name;
//        String [] whereArgs = {null};
//        //参数1 是要更新的表名
//        //参数2 是一个ContentValeus对象
//        //参数3 是where子句
//        db.update("tips_table", cv, whereClause, whereArgs);
//    }
    //删除数据的方法
    public void deleteDB(Context context,String table,String name,String username){
        MyDBHelper dbHelper = new MyDBHelper(context,"tips_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        System.out.println("+++++++++++++++++++++++--- delete ---++++++++++++++++++++++++++++++++");
        String whereClauses = null;
        if (table =="tips_table"||table =="tips_basket"){
            whereClauses = "name=? and username=?";
        }if(table =="wordnote_word"||table =="wordnote_sentence"){
            whereClauses = "english=? and username=?";
        }if(table == "accountbook"){
            whereClauses = "time=? and username=?";
        }
        String [] whereArgs = {name,username};
        //调用delete方法，删除数据
        db.delete(table, whereClauses, whereArgs);
    }

}
