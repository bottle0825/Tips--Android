package com.dfc.tips.com.dfc.tips.action;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dfc.tips.R;
import com.dfc.tips.com.dfc.tips.db.MyDB;
import com.dfc.tips.com.dfc.tips.msg.RoundImageView;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Luser;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nickname;
    private EditText year;
    private EditText month;
    private EditText day;
    private RadioGroup gender;
    private RadioButton boy;
    private RadioButton girl;
    private Button password;
    private Button save;
    private ImageView back;
    private RoundImageView head;
    private boolean note_img_flag = false;//判断有无图片
    private String image_path = null;
    private MyDB myDB = new MyDB();

    public void init(){
        nickname = (EditText) findViewById(R.id.user_change_nickname);
        year = (EditText) findViewById(R.id.user_change_birth_year);
        month = (EditText) findViewById(R.id.user_change_birth_month);
        day = (EditText) findViewById(R.id.user_change_birth_day);
        gender = (RadioGroup) findViewById(R.id.user_change_gender);
        boy = (RadioButton) findViewById(R.id.boy);
        girl = (RadioButton) findViewById(R.id.girl);
        password = (Button) findViewById(R.id.user_change_password);
        password.setOnClickListener(this);
        save = (Button) findViewById(R.id.user_change_save);
        save.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.user_change_back);
        back.setOnClickListener(this);
        head = (RoundImageView) findViewById(R.id.user_change_head);
        head.setOnClickListener(this);

        myDB.readuser(UserActivity.this,Login_username);

        nickname.setText(Luser.getNickname());
        year.setText(Luser.getBirth_year());
        month.setText(Luser.getBirth_month());
        day.setText(Luser.getBirth_day());
        image_path = Luser.getHead();
        if (image_path!=null){
            head.setImageBitmap(BitmapFactory.decodeFile(image_path));
        }
        if (Luser.getGender()!=null&&Luser.getGender().equals("boy")){
            boy.setChecked(true);
        }if(Luser.getGender()!=null&&Luser.getGender().equals("girl")){
            girl.setChecked(true);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // TODO Auto-generated method stub
                if (checkedId == boy.getId())
                {
                    Luser.setGender("boy");
                }
                if (checkedId == girl.getId())
                {
                    Luser.setGender("girl");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_change_head:
                if (!note_img_flag){
                    // 打开本地相册
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // 设定结果返回
                    startActivityForResult(i, 2);
                }
                break;
            case R.id.user_change_password:

                break;
            case R.id.user_change_save:
                Luser.setNickname(nickname.getText().toString());
                Luser.setBirth_year(year.getText().toString());
                Luser.setBirth_month(month.getText().toString());
                Luser.setBirth_day(day.getText().toString());
                Luser.setHead(image_path);
                myDB.deleteuser(UserActivity.this,Login_username);
                myDB.insertuser(UserActivity.this,Luser);
                Intent intent1 = new Intent(UserActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.user_change_back:
                Intent intent2 = new Intent(UserActivity.this,MainActivity.class);
                startActivity(intent2);
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
            head.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
