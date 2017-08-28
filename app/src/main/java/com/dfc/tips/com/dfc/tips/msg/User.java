package com.dfc.tips.com.dfc.tips.msg;

/**
 * Created by dfc on 2017/7/20.
 */

public class User {
    private String username;
    private String nickname;
    private String gender;
    private String birth_year;
    private String birth_month;
    private String birth_day;
    private float money;
    private String  function;
    private String head;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getBirth_month() {
        return birth_month;
    }

    public void setBirth_month(String birth_month) {
        this.birth_month = birth_month;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
            this.money = money;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String  function) {
            this.function = function;
    }
}
