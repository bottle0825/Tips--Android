package com.dfc.tips.com.dfc.tips.action;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfc.tips.R;
import com.dfc.tips.com.dfc.tips.msg.Global_variable;
import com.dfc.tips.com.dfc.tips.msg.Tips;
import com.dfc.tips.com.dfc.tips.db.MyDB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.tips_note;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int IMAGE = 1;
    private ImageView back;
    private ImageView information;
    private ImageView note_img;
    private boolean note_img_flag = false;//判断有无图片
    private DrawerLayout NoteDrawerLayout;
    private RelativeLayout right_message;
    private TextView note_time;
    private TextView note_date;
    private TextView note_weak;
    private TextView note_by;
    private TextView note_wd;
    private TextView note_sentence;
    private EditText note_title;
    private EditText note_content;
    private Button save;
    private Button clear;
    private Tips tips;
    private MyDB myDB;
    public static final int MSG_ONE = 1;
    private boolean flag = false;
    private int pos = -2;
    private String image_path = null;

    public void init(){
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        information = (ImageView) findViewById(R.id.information);
        information.setOnClickListener(this);
        note_img = (ImageView) findViewById(R.id.note_img);
        note_img.setOnClickListener(this);
        NoteDrawerLayout = (DrawerLayout) findViewById(R.id.NoteDrawerLayout);
        right_message = (RelativeLayout) findViewById(R.id.right_message);
        note_time = (TextView) findViewById(R.id.note_time);
        note_date = (TextView) findViewById(R.id.note_date);
        note_weak = (TextView) findViewById(R.id.note_weak);
        note_by = (TextView) findViewById(R.id.note_by);
        note_wd = (TextView) findViewById(R.id.note_wd);
        note_sentence = (TextView) findViewById(R.id.note_sentence);
        note_title = (EditText) findViewById(R.id.note_title);
        note_content = (EditText) findViewById(R.id.note_content);
        save = (Button) findViewById(R.id.note_save);
        save.setOnClickListener(this);
        clear = (Button) findViewById(R.id.note_clear);
        clear.setOnClickListener(this);
        tips = new Tips();
        myDB = new MyDB();

        new TimeThread().start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        init();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null){
            pos = bundle.getInt("position");
            note_title.setText(tips_note.get(pos).getTips_name());
            note_content.setText(tips_note.get(pos).getTips_content());
            image_path = tips_note.get(pos).getTips_img();
            if (image_path!=null){
                note_img.setImageBitmap(BitmapFactory.decodeFile(image_path));
            }
            flag = true;
        }



        NoteDrawerLayout.setDrawerLockMode(NoteDrawerLayout.LOCK_MODE_UNLOCKED);
            //菜单控制
        NoteDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                /**
                 * 当抽屉滑动状态改变的时候被调用
                 * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
                 * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
                 */
                @Override
                public void onDrawerStateChanged(int arg0) {
                    Log.i("drawer", "drawer的状态：" + arg0);
                }

                /**
                 * 当抽屉被滑动的时候调用此方法
                 * arg1 表示 滑动的幅度（0-1）
                 */
                @Override
                public void onDrawerSlide(View arg0, float arg1) {
                    Log.i("drawer", arg1 + "");
                    setNoteMessage();
                }

                /**
                 * 当一个抽屉被完全打开的时候被调用
                 */
                @Override
                public void onDrawerOpened(View arg0) {
                    Log.i("drawer", "抽屉被完全打开了！");
                }

                /**
                 * 当一个抽屉完全关闭的时候调用此方法
                 */
                @Override
                public void onDrawerClosed(View arg0) {
                    Log.i("drawer", "抽屉被完全关闭了！");
                }


        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        if (NoteDrawerLayout!=null){
            if (NoteDrawerLayout.isDrawerOpen(right_message)){
                NoteDrawerLayout.closeDrawers();
            }else super.onBackPressed();
        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //通过消息的内容msg.what  分别更新ui
            switch (msg.what) {
                case MSG_ONE:
                    //获取到系统当前时间 long类型
                    long time = System.currentTimeMillis();
                    //将long类型的时间转换成日历格式
                    Date data = new Date(time);
                    // 转换格式，年月日时分秒 星期  的格式
                    SimpleDateFormat sdfd = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat sdfw = new SimpleDateFormat("EEE");
                    SimpleDateFormat sdft = new SimpleDateFormat("HH:mm");
                    //显示在textview上，通过转换格式
                    note_time.setText(sdft.format(data));
                    note_weak.setText(sdfw.format(data));
                    note_date.setText(sdfd.format(data));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        //获取到系统当前时间 long类型
        long time = System.currentTimeMillis();
        //将long类型的时间转换成日历格式
        Date data = new Date(time);
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy/MM/dd");
        switch (v.getId()){
            case R.id.back:
                Intent intent1 = new Intent(NoteActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.information:
                NoteDrawerLayout.openDrawer(right_message);
                break;
            case R.id.note_save:
                tips.setTips_name(note_title.getText().toString());
                tips.setTips_content(note_content.getText().toString());
                tips.setTips_type("common");
                tips.setTips_createtime(sdfd.format(data));
                Log.e("note",image_path+"****************************************************");
                tips.setTips_img(image_path);
                tips.setTips_username(Login_username);
                if (flag){
                    myDB.deleteDB(NoteActivity.this,"tips_table",tips_note.get(pos).getTips_name(),Login_username);
                }
                if(myDB.queryDB(NoteActivity.this,"tips_table",tips.getTips_name(),Login_username)){
                    Intent intent2 = new Intent(NoteActivity.this,MainActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable( "com.tutor.objecttran.par", tips);
                    intent2.putExtras(mBundle);
                    startActivity(intent2);
                }else {
                    Toast.makeText(NoteActivity.this,"该tips标题已存在，请重新设置标题",Toast.LENGTH_LONG).show();
                    NoteDrawerLayout.closeDrawers();
                }
                break;
            case R.id.note_clear:
                note_title.setText("");
                note_content.setText("");
                NoteDrawerLayout.closeDrawers();
                break;
            case R.id.note_img:
                if (!note_img_flag){
                    // 打开本地相册
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // 设定结果返回
                    startActivityForResult(i, 2);
                }
                break;
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null&&requestCode==2) {
            //打开相册并选择照片，这个方式选择单张
            // 获取返回的数据，这里是android自定义的Uri地址
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            // 获取选择照片的数据视图
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            // 从数据视图中获取已选择图片的路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            image_path = picturePath;
            cursor.close();
            // 将图片显示到界面上
            note_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            // do-while  一 什么什么 就
            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1000);
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = MSG_ONE;
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
    public void setNoteMessage() {
        int wd = 1;//单词数
        int by = 0;//字符数
        int sentence = 1;//句子数
        String s = note_content.getText().toString().trim();//获取数据
        for (int i = 0; i < s.length(); i++) { //得到所输入的数据的长度，并遍历
            char c = s.charAt(i);
            if (c == ' ') wd++; //单词
            if (c == '.' || c == '!' || c == '?'||c == '。')
                if (i > 0 && s.charAt(i - 1) != '.' && s.charAt(i - 1) != '?' && s.charAt(i - 1) != '!') {
                    sentence++;
                    wd++;
                }
            by++;
        }
        note_by.setText(""+by);
        note_wd.setText(""+wd);
        note_sentence.setText(""+sentence);
    }
}




