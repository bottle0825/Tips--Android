package com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dfc.tips.R;
import com.dfc.tips.com.dfc.tips.action.MainActivity;
import com.dfc.tips.com.dfc.tips.action.NoteActivity;
import com.dfc.tips.com.dfc.tips.db.MyDB;
import com.dfc.tips.com.dfc.tips.msg.Account;
import com.dfc.tips.com.dfc.tips.msg.Global_variable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.accounts;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.accounts_clothes;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.accounts_entertainment;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.accounts_food;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.accounts_investment;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.consumption_clothes;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.consumption_day;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.consumption_entertainment;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.consumption_investment;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.consumption_month;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.tips_basket;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.tips_note;

public class AccountInvestmentFragment extends Fragment implements View.OnClickListener{


    View view;

    private Context context;
    private Account addAccount = new Account();
    private MyDB myDB = new MyDB();
    private TextView add;
    private TextView totalamount;
    private EditText addaccountA;
    private EditText addaccountM;
    private Global_variable mGlobal_variable;
    public  static AccountFragment mParentFragment;

    public static AccountInvestmentFragment newInstance(AccountFragment parentFragment) {

        Bundle args = new Bundle();
        mParentFragment=parentFragment;
        AccountInvestmentFragment fragment = new AccountInvestmentFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init(){
        add = (TextView) view.findViewById(R.id.accountbook_investment_add);
        add.setOnClickListener(this);
        totalamount = (TextView) view.findViewById(R.id.accountbook_investment_totalamount);

        update_consumption();
        createList();

    }
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_investment, container, false);

        init();
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update_consumption(){
        consumption_investment = 0;
        consumption_month = 0;
        consumption_day = 0;

        accounts = new ArrayList<Account>();
        accounts_investment = new ArrayList<Account>();
        myDB.readDB(context,"accountbook",Login_username);
        for (int i=0;i<accounts.size();i++){
            String s=(accounts.get(i).getTime()).substring(5,7);
            if (s.equals(gettime().substring(5,7))){
                consumption_month = consumption_month+accounts.get(i).getAmount();
                String sd = (accounts.get(i).getTime()).substring(8,10);
                if (sd.equals(gettime().substring(8,10))){
                    consumption_day = consumption_day+accounts.get(i).getAmount();
                }
                if (accounts.get(i).getType().equals("investment")){
                    accounts_investment.add(accounts.get(i));
                    consumption_investment = consumption_investment+accounts.get(i).getAmount();
                }
            }
        }
        totalamount.setText(consumption_investment+"");
        mParentFragment.update_consumption();
    }

    public void createList(){
        //创建一个List集合
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = accounts_investment.size()-1; i >=0; i--) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("time", accounts_investment.get(i).getTime());
            listItem.put("amount", accounts_investment.get(i).getAmount());
            listItem.put("message", accounts_investment.get(i).getMessage());
            listItems.add(listItem);
        }
        //创建一个简单SimpleAdapter
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.accountlist,
                new String[]{"time", "amount", "message"},
                new int[]{R.id.accountbook_time, R.id.accountbook_amount, R.id.accountbook_message}
        );
        final SwipeMenuListView list = (SwipeMenuListView) view.findViewById(R.id.accountbook_investmentlist);
        list.setAdapter(simpleAdapter);
        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dip2px(getActivity(),90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        list.setMenuCreator(creator);
        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(final int myposition, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        myDB.deleteDB(context,"accountbook",accounts_investment.get(accounts_investment.size()-myposition-1).getTime().toString(),Login_username);
                        listItems.remove(myposition);
                        simpleAdapter.notifyDataSetChanged();
                        Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                        init();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accountbook_investment_add:
                Toast.makeText(context,"添加",Toast.LENGTH_SHORT).show();
                addalert();
                init();
                break;
        }
    }
    public String gettime(){
        long time = System.currentTimeMillis();
        //将long类型的时间转换成日历格式
        Date data = new Date(time);
        // 转换格式，年月日时分秒 星期  的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(data);
    }
    public void addalert(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View adda = layoutInflater.inflate(R.layout.addalertaccount, null);
        new AlertDialog.Builder(context)
                .setView(adda)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addaccountA = (EditText) adda.findViewById(R.id.accountbook_addaccount_amount);
                        addaccountM = (EditText) adda.findViewById(R.id.accountbook_addaccount_message);
                        addAccount.setTime(gettime());
                        addAccount.setAmount(Float.parseFloat(addaccountA.getText().toString()));
                        addAccount.setType("investment");
                        addAccount.setMessage(addaccountM.getText().toString());
                        addAccount.setUsername(Login_username);
                        myDB.insertaccount(context,addAccount);
                        accounts_food.add(addAccount);
                        Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
                        init();
                    }
                })
                .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }
}
