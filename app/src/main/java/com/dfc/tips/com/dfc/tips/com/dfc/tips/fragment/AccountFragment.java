package com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfc.tips.R;
import com.dfc.tips.com.dfc.tips.db.MyDB;
import com.dfc.tips.com.dfc.tips.msg.Account;
import com.dfc.tips.com.dfc.tips.msg.Global_variable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.accounts;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.consumption_day;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.consumption_month;

public class AccountFragment extends Fragment implements View.OnClickListener{


    View view;
    AccountFoodFragment aff;
    AccountClothesFragment acf;
    AccountEntertainmentFragment aef;
    AccountInvestmentFragment aif;
    AccountOtherFragment aof;

    private RelativeLayout Ff;
    private ImageView Ff_image;
    private TextView Ff_text;
    private RelativeLayout Cf;
    private ImageView Cf_image;
    private TextView Cf_text;
    private RelativeLayout Ef;
    private ImageView Ef_image;
    private TextView Ef_text;
    private RelativeLayout If;
    private ImageView If_image;
    private TextView If_text;
    private RelativeLayout Of;
    private ImageView Of_image;
    private TextView Of_text;
    private TextView Cm;
    private TextView Cd;
    private TextView Bt;



    private Context context;
    private MyDB myDB = new MyDB();
    private Global_variable mGlobal_variable;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update_consumption(){
        accounts = new ArrayList<Account>();
        consumption_month = 0;
        consumption_day = 0;
        myDB.readDB(context,"accountbook",Login_username);
        for (int i = 0;i<accounts.size();i++){
            String s=(accounts.get(i).getTime()).substring(5,7);
            if (s.equals(gettime().substring(5,7))){
                consumption_month = consumption_month+accounts.get(i).getAmount();
                String sd = (accounts.get(i).getTime()).substring(8,10);
                if (sd.equals(gettime().substring(8,10))){
                    consumption_day = consumption_day+accounts.get(i).getAmount();
                }
            }
        }
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(3000.0-consumption_month);//format 返回的是字符串
        Cm.setText(consumption_month+"");
        Cd.setText(consumption_day+"");
        Bt.setText(p);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void init(){
        Ff = (RelativeLayout) view.findViewById(R.id.accountbook_fragment_food);
        Ff.setOnClickListener(this);
        Ff_image = (ImageView) view.findViewById(R.id.accountbook_image_food);
        Ff_text = (TextView) view.findViewById(R.id.accountbook_text_food);
        Cf = (RelativeLayout) view.findViewById(R.id.accountbook_fragment_clothes);
        Cf.setOnClickListener(this);
        Cf_image = (ImageView) view.findViewById(R.id.accountbook_image_clothes);
        Cf_text = (TextView) view.findViewById(R.id.accountbook_text_clothes);
        Ef = (RelativeLayout) view.findViewById(R.id.accountbook_fragment_entertainment);
        Ef.setOnClickListener(this);
        Ef_image = (ImageView) view.findViewById(R.id.accountbook_image_entertainment);
        Ef_text = (TextView) view.findViewById(R.id.accountbook_text_entertainment);
        If = (RelativeLayout) view.findViewById(R.id.accountbook_fragment_investment);
        If.setOnClickListener(this);
        If_image = (ImageView) view.findViewById(R.id.accountbook_image_investment);
        If_text = (TextView) view.findViewById(R.id.accountbook_text_investment);
        Of = (RelativeLayout) view.findViewById(R.id.accountbook_fragment_other);
        Of.setOnClickListener(this);
        Of_image = (ImageView) view.findViewById(R.id.accountbook_image_other);
        Of_text = (TextView) view.findViewById(R.id.accountbook_text_other);
        Cm = (TextView) view.findViewById(R.id.consumption_month);
        Cd = (TextView) view.findViewById(R.id.consumption_day);
        Bt = (TextView) view.findViewById(R.id.balance_month);

        update_consumption();
    }
    public void resetbutton(){
        Ff_image.setImageResource(R.drawable.food);
        Ff_text.setTextColor(this.getResources().getColor(R.color.text));
        Cf_image.setImageResource(R.drawable.clothes);
        Cf_text.setTextColor(this.getResources().getColor(R.color.text));
        Ef_image.setImageResource(R.drawable.entertainment);
        Ef_text.setTextColor(this.getResources().getColor(R.color.text));
        If_image.setImageResource(R.drawable.investment);
        If_text.setTextColor(this.getResources().getColor(R.color.text));
        Of_image.setImageResource(R.drawable.other);
        Of_text.setTextColor(this.getResources().getColor(R.color.text));
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        init();

//        String s=(accounts.get(0).getTime()).substring(5,7);
//        int I = Integer.parseInt(s);
//        System.out.println("++++++++++++++++++++++++++++++++++++++++++++-----------********************"+s+"*********"+I);
        setDefaultFragment();
        return view;
    }

    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        aff = AccountFoodFragment.newInstance(this);
        resetbutton();
        Ff_image.setImageResource(R.drawable.food_click);
        Ff_text.setTextColor(this.getResources().getColor(R.color.top));
        transaction.replace(R.id.accountbook_content, aff);
        transaction.commit();
    }
    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public String gettime(){
        long time = System.currentTimeMillis();
        //将long类型的时间转换成日历格式
        Date data = new Date(time);
        // 转换格式，年月日时分秒 星期  的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(data);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        //开启Fragment事务
        android.app.FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()){
            case R.id.accountbook_fragment_food:
//                Toast.makeText(context,"点击食物",Toast.LENGTH_SHORT).show();
                if (aff == null) {
                    aff = AccountFoodFragment.newInstance(this);
                }
                resetbutton();
                Ff_image.setImageResource(R.drawable.food_click);
                Ff_text.setTextColor(this.getResources().getColor(R.color.top));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.accountbook_content, aff);
                transaction.commit();
                break;
            case R.id.accountbook_fragment_clothes:
//                Toast.makeText(context,"点击衣服",Toast.LENGTH_SHORT).show();
                if (acf == null) {
                    acf = AccountClothesFragment.newInstance(this);
                }
                resetbutton();
                Cf_image.setImageResource(R.drawable.clothes_click);
                Cf_text.setTextColor(this.getResources().getColor(R.color.top));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.accountbook_content, acf);
                transaction.commit();
                break;
            case R.id.accountbook_fragment_entertainment:
//                Toast.makeText(context,"点击娱乐",Toast.LENGTH_SHORT).show();
                if (aef == null) {
                    aef = AccountEntertainmentFragment.newInstance(this);
                }resetbutton();
                Ef_image.setImageResource(R.drawable.entertainment_click);
                Ef_text.setTextColor(this.getResources().getColor(R.color.top));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.accountbook_content, aef);
                transaction.commit();
                break;
            case R.id.accountbook_fragment_investment:
//                Toast.makeText(context,"点击投资",Toast.LENGTH_SHORT).show();
                if (aif == null) {
                    aif = AccountInvestmentFragment.newInstance(this);
                }resetbutton();
                If_image.setImageResource(R.drawable.investment_click);
                If_text.setTextColor(this.getResources().getColor(R.color.top));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.accountbook_content, aif);
                transaction.commit();
                break;
            case R.id.accountbook_fragment_other:
//                Toast.makeText(context,"点击其他",Toast.LENGTH_SHORT).show();
                if (aof == null) {
                    aof = AccountOtherFragment.newInstance(this);
                }resetbutton();
                Of_image.setImageResource(R.drawable.other_click);
                Of_text.setTextColor(this.getResources().getColor(R.color.top));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.accountbook_content, aof);
                transaction.commit();
                break;

        }
    }
}