package com.dfc.tips.com.dfc.tips.msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfc on 2017/7/12.
 */

public class Tips_note {
    public static String Login_username = "localuser";
    public static User Luser = new User();

    public static List<Tips> tips_note = new ArrayList<>();
    public static List<Tips> tips_basket = new ArrayList<>();
    public static List<Words> words = new ArrayList<>();
    public static List<Sentences> sentences = new ArrayList<>();
    public static List<Account> accounts = new ArrayList<>();
    public static List<Account> accounts_food = new ArrayList<>();
    public static List<Account> accounts_clothes = new ArrayList<>();
    public static List<Account> accounts_entertainment = new ArrayList<>();
    public static List<Account> accounts_investment = new ArrayList<>();
    public static List<Account> accounts_other = new ArrayList<>();

    public static float consumption_month;
    public static float consumption_day;
    public static float consumption_food;
    public static float consumption_clothes;
    public static float consumption_entertainment;
    public static float consumption_investment;
    public static float consumption_other;

}
