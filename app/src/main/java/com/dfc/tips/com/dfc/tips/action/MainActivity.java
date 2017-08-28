package com.dfc.tips.com.dfc.tips.action;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment.AccountFragment;
import com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment.BasketFragment;
import com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment.WordFragment;
import com.dfc.tips.com.dfc.tips.msg.Global_variable;
import com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment.NoteFragment;
import com.dfc.tips.R;
import com.dfc.tips.com.dfc.tips.msg.RoundImageView;
import com.dfc.tips.com.dfc.tips.msg.Tips;
import com.dfc.tips.com.dfc.tips.db.MyDB;
import com.dfc.tips.com.dfc.tips.msg.User;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Luser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout MainDrawerLayout = null;
    private LinearLayout left_menu;
    private ImageView menu;
    private RoundImageView head;
    private LinearLayout addLinearLayout;
    private Global_variable global_variable;
    private RelativeLayout menu_login;
    private LinearLayout menu_unlogin;
    private boolean flag;
    private Button login;
    private Button cancel;
    private NoteFragment notefragment;
    private BasketFragment basketfragment;
    private WordFragment wordfragment;
    private AccountFragment accountfragment;
    private LinearLayout user;
    private LinearLayout note;
    private LinearLayout basket;
    private LinearLayout wordnote;
    private LinearLayout accountbook;
    private TextView title;
    private TextView username;
    private Tips tip;
    private MyDB myDB = new MyDB();
    private CheckBox add_Layout_worknote;
    private CheckBox add_Layout_accountbook;
    private String image_path = null;


    public void init() {
        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);
        head = (RoundImageView) findViewById(R.id.main_head);
        menu_login = (RelativeLayout) findViewById(R.id.menu_login);
        menu_unlogin = (LinearLayout) findViewById(R.id.menu_unlogin);
        left_menu = (LinearLayout) findViewById(R.id.left_menu);
        addLinearLayout = (LinearLayout) findViewById(R.id.Layout_add);
        addLinearLayout.setOnClickListener(this);
        login = (Button) findViewById(R.id.Login);
        login.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        title = (TextView) findViewById(R.id.main_title);
        username = (TextView) findViewById(R.id.username);
        username.setText(""+Login_username);
        user = (LinearLayout) findViewById(R.id.Layout_username);
        user.setOnClickListener(this);
        note = (LinearLayout) findViewById(R.id.Layout_note);
        note.setOnClickListener(this);
        basket = (LinearLayout) findViewById(R.id.Layout_basket);
        basket.setOnClickListener(this);
        wordnote = (LinearLayout) findViewById(R.id.Layout_wordnote);
        wordnote.setOnClickListener(this);
        accountbook = (LinearLayout) findViewById(R.id.Layout_account);
        accountbook.setOnClickListener(this);
        MainDrawerLayout = (DrawerLayout) findViewById(R.id.MainDrawerLayout);


        image_path = Luser.getHead();
        if (image_path!=null){
            head.setImageBitmap(BitmapFactory.decodeFile(image_path));
        }
        tip = getIntent().getParcelableExtra("com.tutor.objecttran.par");
        Log.e("note",Luser.getUsername()+"****************************************************");

        if(Luser.getFunction()!=null&&Luser.getFunction().equals("s")){
            wordnote.setVisibility(View.GONE);
            accountbook.setVisibility(View.GONE);
        }
        if(Luser.getFunction()!=null&&Luser.getFunction().equals("w")){
            wordnote.setVisibility(View.VISIBLE);
            accountbook.setVisibility(View.GONE);
        }
        if(Luser.getFunction()!=null&&Luser.getFunction().equals("a")){
            accountbook.setVisibility(View.VISIBLE);
            wordnote.setVisibility(View.GONE);
        }
        if(Luser.getFunction()!=null&&Luser.getFunction().equals("aw")){
            wordnote.setVisibility(View.VISIBLE);
            accountbook.setVisibility(View.VISIBLE);
        }

        if (tip!=null){
            Log.e("note",tip.getTips_img()+"****************************************************");
            myDB.insert(MainActivity.this,"tips_table",tip);
        }
        resetmenu();
        note.setBackgroundColor(getResources().getColor(R.color.blackbutton));
        setDefaultFragment();
    }
    public void resetmenu(){
        note.setBackgroundColor(getResources().getColor(R.color.transparent));
        basket.setBackgroundColor(getResources().getColor(R.color.transparent));
        wordnote.setBackgroundColor(getResources().getColor(R.color.transparent));
        accountbook.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        global_variable = (Global_variable) getApplication();
        init();
        //判断是不是登录状态
        flag = global_variable.getLogin_flag();
        if (flag){
            menu_unlogin.setVisibility(View.GONE);
            menu_login.setVisibility(View.VISIBLE);
        }else{
            menu_login.setVisibility(View.GONE);
            menu_unlogin.setVisibility(View.VISIBLE);
        }
        MainDrawerLayout.setDrawerLockMode(MainDrawerLayout.LOCK_MODE_UNLOCKED);
        //菜单控制
        MainDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
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

    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();

        notefragment = new NoteFragment();
        transaction.replace(R.id.tips_content, notefragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (MainDrawerLayout!=null){
            if (MainDrawerLayout.isDrawerOpen(left_menu)){
                MainDrawerLayout.closeDrawers();
            }else super.onBackPressed();
        }
    }


    @Override
    public void onClick(final View v) {
        Log.i("click", "onClick: ");
//        init();
        FragmentManager fm = getFragmentManager();
//         开启Fragment事务
        android.app.FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()){
            case R.id.Login:
                Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.cancel:
                global_variable.setLogin_flag(false);
                finish();
                Login_username = "localuser";
                Luser = new User();
                Intent intent2 = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.menu:
                MainDrawerLayout.openDrawer(left_menu);
                break;
            case R.id.Layout_username:
                Intent intent3 = new Intent(MainActivity.this,UserActivity.class);
                startActivity(intent3);
                break;
            case R.id.Layout_note:
                if (notefragment == null) {
                    notefragment = new NoteFragment();
                }
                title.setText("手记");
                resetmenu();
                note.setBackgroundColor(getResources().getColor(R.color.blackbutton));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.tips_content, notefragment);
                transaction.commit();
                MainDrawerLayout.closeDrawers();
                break;
            case R.id.Layout_basket:
                if (basketfragment == null) {
                    basketfragment = new BasketFragment();
                }
                title.setText("废纸篓");
                resetmenu();
                basket.setBackgroundColor(getResources().getColor(R.color.blackbutton));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.tips_content, basketfragment);
                transaction.commit();
                MainDrawerLayout.closeDrawers();
                break;
            case R.id.Layout_wordnote:
                if (wordfragment == null) {
                    wordfragment = new WordFragment();
                }
                title.setText("单词本");
                resetmenu();
                wordnote.setBackgroundColor(getResources().getColor(R.color.blackbutton));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.tips_content, wordfragment);
                transaction.commit();
                MainDrawerLayout.closeDrawers();
                break;
            case R.id.Layout_account:
                if (accountfragment == null) {
                    accountfragment = new AccountFragment();
                }
                title.setText("随身账本");
                resetmenu();
                accountbook.setBackgroundColor(getResources().getColor(R.color.blackbutton));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.tips_content, accountfragment);
                transaction.commit();
                MainDrawerLayout.closeDrawers();
                break;
            case R.id.Layout_add://添加自定义组建
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View adda = layoutInflater.inflate(R.layout.alert_addfunction, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(adda);
                add_Layout_worknote = (CheckBox) adda.findViewById(R.id.Layout_add_wordnote);
                add_Layout_accountbook = (CheckBox) adda.findViewById(R.id.Layout_add_accountbook);
                if(Luser.getFunction()!=null&&Luser.getFunction().equals("w")){
                    add_Layout_worknote.setChecked(true);
                    wordnote.setVisibility(View.VISIBLE);
                    add_Layout_accountbook.setChecked(false);
                    accountbook.setVisibility(View.GONE);
                }
                if(Luser.getFunction()!=null&&Luser.getFunction().equals("a")){
                    add_Layout_accountbook.setChecked(true);
                    accountbook.setVisibility(View.VISIBLE);
                    add_Layout_worknote.setChecked(false);
                    wordnote.setVisibility(View.GONE);
                }
                if(Luser.getFunction()!=null&&Luser.getFunction().equals("aw")){
                    add_Layout_worknote.setChecked(true);
                    wordnote.setVisibility(View.VISIBLE);
                    add_Layout_accountbook.setChecked(true);
                    accountbook.setVisibility(View.VISIBLE);
                }
                if(Luser.getFunction()!=null&&Luser.getFunction().equals("s")){
                    add_Layout_worknote.setChecked(false);
                    wordnote.setVisibility(View.GONE);
                    add_Layout_accountbook.setChecked(false);
                    accountbook.setVisibility(View.GONE);
                }
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (add_Layout_worknote.isChecked()){
                            wordnote.setVisibility(View.VISIBLE);
                        }else {
                            wordnote.setVisibility(View.GONE);
                        }
                        if (add_Layout_accountbook.isChecked()){
                            accountbook.setVisibility(View.VISIBLE);
                        }else {
                            accountbook.setVisibility(View.GONE);
                        }

                        if (!add_Layout_worknote.isChecked()&&!add_Layout_accountbook.isChecked()){
                            Luser.setFunction("s");
                        }
                        if (add_Layout_worknote.isChecked()&&!add_Layout_accountbook.isChecked()){
                            Luser.setFunction("w");
                        }
                        if (!add_Layout_worknote.isChecked()&&add_Layout_accountbook.isChecked()){
                            Luser.setFunction("a");
                        }
                        if(add_Layout_worknote.isChecked()&&add_Layout_accountbook.isChecked()){
                            Luser.setFunction("aw");
                        }

                        myDB.deleteuser(MainActivity.this,Login_username);
                        myDB.insertuser(MainActivity.this,Luser);
                    }
                }).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
                break;
            default:
                break;

        }
    }

}


