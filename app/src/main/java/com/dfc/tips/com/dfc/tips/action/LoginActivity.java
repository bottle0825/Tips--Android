package com.dfc.tips.com.dfc.tips.action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    Button Login;
    Button Register;
    ImageView backButton;
    private Global_variable mGlobal_variable;
    private MyDB myDB = new MyDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //获得全局变量
        mGlobal_variable = (Global_variable) getApplication();
        Login = (Button) findViewById(R.id.Login);
        Register = (Button) findViewById(R.id.Register);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            //登陆点击事件
            public void onClick(View v) {
                final String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                OkGo.post(mGlobal_variable.Server_Address+"login").tag(this)
                        .params("user.name",username)
                        .params("user.password",password)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                Gson gson = new Gson();
                                LoginMsg msg = gson.fromJson(s,LoginMsg.class);
                                if(msg.getMsg().equals("success")){
                                    mGlobal_variable.setLogin_flag(true);
                                    Login_username = username;
                                    Luser.setUsername(Login_username);
                                    Log.e("note",Luser.getUsername()+"--------------------------------------------------------");
                                    if(myDB.queryuser(LoginActivity.this,Login_username)){
                                        myDB.insertuser(LoginActivity.this,Luser);
                                    }
                                    Luser.setUsername("");
                                    myDB.readuser(LoginActivity.this,Login_username);
                                    Toast.makeText(LoginActivity.this,"登录成功"+Login_username,Toast.LENGTH_LONG).show();
                                    //界面跳转至主界面
                                    Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);

                                }else if(msg.getMsg().equals("error")){
                                    Toast.makeText(LoginActivity.this,"账号不存在或密码错误",Toast.LENGTH_LONG).show();
                                    ((EditText)findViewById(R.id.username)).setText("");
                                    ((EditText)findViewById(R.id.password)).setText("");
                                }else {
                                    Toast.makeText(LoginActivity.this,"连接服务器失败",Toast.LENGTH_LONG).show();
                                }
                            }
                        });



            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            //注册点击事件
            public void onClick(View v) {
                //界面跳转至注册界面
                Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        backButton= (ImageView)findViewById(R.id.back) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //界面跳转至主界面
                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
