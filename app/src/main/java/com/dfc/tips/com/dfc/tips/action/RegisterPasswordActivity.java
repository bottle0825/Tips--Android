package com.dfc.tips.com.dfc.tips.action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dfc.tips.com.dfc.tips.db.MyDB;
import com.dfc.tips.com.dfc.tips.msg.Global_variable;
import com.dfc.tips.com.dfc.tips.msg.LoginMsg;
import com.dfc.tips.R;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Luser;

public class RegisterPasswordActivity extends AppCompatActivity {

    private ImageView back;
    private Button confirm;
    private EditText password;
    private EditText repassword;
    private String passwordString;
    private String repasswordString;
    private Global_variable mGlobal_variable;
    private MyDB myDB = new MyDB();

    protected void init(){
        back = (ImageView) findViewById(R.id.back);
        confirm = (Button) findViewById(R.id.confirm);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        mGlobal_variable = (Global_variable) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        Bundle bundle = this.getIntent().getExtras();
        final String phonenum = bundle.getString("phonenum");
        if (bundle!=null){
            Toast.makeText(RegisterPasswordActivity.this,phonenum,Toast.LENGTH_LONG).show();
        }

        init();



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordString = password.getText().toString();
                repasswordString = repassword.getText().toString();
                if (passwordString.equals(repasswordString)){
                    OkGo.post(mGlobal_variable.Server_Address+"rejister").tag(this)
                            .params("user.name",phonenum)
                            .params("user.password",passwordString)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Gson gson = new Gson();
                                    LoginMsg msg = gson.fromJson(s,LoginMsg.class);
                                    if(msg.getMsg().equals("success")){
                                        mGlobal_variable.setLogin_flag(true);
                                        Login_username = phonenum;
                                        Luser.setUsername(Login_username);
                                        if(myDB.queryuser(RegisterPasswordActivity.this,Login_username)){
                                            myDB.insertuser(RegisterPasswordActivity.this,Luser);
                                        }
                                        myDB.readuser(RegisterPasswordActivity.this,Login_username);
                                        Toast.makeText(RegisterPasswordActivity.this,"注册成功"+Login_username,Toast.LENGTH_LONG).show();
                                        //界面跳转至主界面
                                        Intent intent =new Intent(RegisterPasswordActivity.this,UserActivity.class);
                                        startActivity(intent);

                                    }else if(msg.getMsg().equals("error")){
                                        Toast.makeText(RegisterPasswordActivity.this,"账号已存在",Toast.LENGTH_SHORT);
                                        Intent intent =new Intent(RegisterPasswordActivity.this,RegisterActivity.class);
                                        startActivity(intent);
                                        password.setText("");
                                        repassword.setText("");
                                    }else {
                                        Toast.makeText(RegisterPasswordActivity.this,"连接服务器失败",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(RegisterPasswordActivity.this,"两次密码不一致，请重新输入",Toast.LENGTH_LONG).show();
                    password.setText("");
                    repassword.setText("");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPasswordActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


    }


}
