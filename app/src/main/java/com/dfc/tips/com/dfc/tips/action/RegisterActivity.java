package com.dfc.tips.com.dfc.tips.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dfc.tips.R;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText cord;
    private String cordString;
    private EditText phone;
    private String phoneString;
    private int phoneLength;
    private Button getcord;
    private Button register;
    private ImageView back;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();//初始化控件

        final Context context = RegisterActivity.this;
        final String AppKey = "1f267e835242b";
        final String AppSecret = "ff20f3c6e6f8aa488009e9084b366215";

        MobSDK.init(context, AppKey, AppSecret);      // 初始化 SDK 单例，可以多次调用
        EventHandler eventHandler = new EventHandler(){    // 操作回调
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);   // 注册回调接口


    }
    protected void init(){
        getcord = (Button) findViewById(R.id.getcord);
        register = (Button) findViewById(R.id.Register);
        getcord.setOnClickListener(this);
        register.setOnClickListener(this);
        phone = (EditText) findViewById(R.id.phone);
        cord = (EditText) findViewById(R.id.cord);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        phoneLength = 0;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.getcord:
                phoneString = phone.getText().toString();
                phoneLength = phoneString.length();
                if (phoneLength!=11){
                    Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    phone.setText("");
                }else{
                    SMSSDK.getVerificationCode("86", phoneString); // 发送验证码给号码的 phoneNumber 的手机
                    cord.requestFocus();
                }
                break;
            case R.id.Register:
                cordString = cord.getText().toString();
                SMSSDK.submitVerificationCode("86", phoneString, cordString);
                flag = false;
//                Intent intent = new Intent(RegisterActivity.this, RegisterPasswordActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("phonenum",phone.getText().toString());
//                intent.putExtras(bundle);
//                startActivity(intent);
                break;
            default:
                break;
        }

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if (result == SMSSDK.RESULT_COMPLETE) {
                // 如果操作成功
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 校验验证码，返回校验的手机和国家代码
                    Toast.makeText(RegisterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, RegisterPasswordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phonenum",phoneString);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功，true为智能验证，false为普通下发短信
                    Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                // 如果操作失败
                if (flag) {
                    Toast.makeText(RegisterActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    cord.setText("");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler(); // 注销回调接口
    }
}

