package com.dfc.tips.com.dfc.tips.msg;

import android.app.Application;

/**
 * Created by dfc on 2017/7/5.
 */

public class Global_variable extends Application{
    public boolean Login_flag;
    public String Server_Address;

    public boolean getLogin_flag(){
        return Login_flag;
    }
    public void setLogin_flag(boolean login_flag) {
        this.Login_flag = login_flag;
    }
    @Override
    public void onCreate(){
        Login_flag = false;
        Server_Address = "http://192.168.15.36:8080/ssh2_test/";
        super.onCreate();
    }
}
