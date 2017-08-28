package com.dfc.tips.com.dfc.tips.action;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dfc.tips.R;

public class WelcomeActivity extends AppCompatActivity {

    private final long SPLASH_LENGTH = 2000;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.postDelayed(new Runnable() {  //使用handler的postDelayed实现延时跳转

            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_LENGTH);//2秒后跳转至应用主界面MainActivity
    }
}
