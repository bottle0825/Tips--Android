package com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.dfc.tips.com.dfc.tips.msg.Global_variable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.tips_note;

public class NoteFragment extends Fragment implements View.OnClickListener{


    View view;

    private Context context;
    private MyDB myDB = new MyDB();
    private TextView day;
    private TextView minute;
    private TextView time_type;
    private TextView note_count;
    private LinearLayout add;
    private Global_variable mGlobal_variable;
    public static final int MSG_ONE = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public void init(){
        day = (TextView) view.findViewById(R.id.note_time_day);
        minute = (TextView) view.findViewById(R.id.note_time_minute);
        time_type = (TextView) view.findViewById(R.id.note_time_type);
        add = (LinearLayout) view.findViewById(R.id.note_add);
        add.setOnClickListener(this);
        note_count = (TextView) view.findViewById(R.id.note_count);

        updatelist();
        new TimeThread().start();
    }
    public void updatelist(){
        tips_note = new ArrayList<>();
        System.out.println("+++++++++++++++++++++++--- check ---++++++++++++++++++++++++++++++++"+mGlobal_variable);
        myDB.readDB(getActivity(),"tips_table",Login_username);
        note_count.setText(""+tips_note.size());
        createlist();
    }



    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);
        init();


        return view;
    }

    public void createlist(){
        //创建一个List集合
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = tips_note.size() - 1; i >= 0; i--) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("tips_day", tips_note.get(i).getTips_createtime());
            listItem.put("tips_title", tips_note.get(i).getTips_name());
            listItem.put("tips_content", tips_note.get(i).getTips_content());
            listItem.put("tips_image", R.drawable.note);
            listItems.add(listItem);
        }
        //创建一个简单SimpleAdapter
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.tipstist,
                new String[]{"tips_day", "tips_title", "tips_content", "tips_image"},
                new int[]{R.id.tips_day, R.id.tips_title, R.id.tips_content, R.id.tips_images}
        );
        final SwipeMenuListView list = (SwipeMenuListView) view.findViewById(R.id.tips_list);
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = tips_note.size() - position - 1;
//                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), NoteActivity.class);
                Bundle pos = new Bundle();
                pos.putInt("position", position);
                intent.putExtras(pos);
                startActivity(intent);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dip2px(getActivity(),90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

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
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        position = tips_note.size() - position - 1;
                        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(), NoteActivity.class);
                        Bundle pos = new Bundle();
                        pos.putInt("position", position);
                        intent.putExtras(pos);
                        startActivity(intent);
                        break;
                    case 1:
                        // delete
                        int poss = position;
                        position = tips_note.size() - position - 1;
                        String deletename = tips_note.get(position).getTips_name();
                        if (!tips_note.get(position).getTips_username().equals("localuser")){
                            myDB.insert(getActivity(),"tips_basket",tips_note.get(position));
                        }
                        myDB.deleteDB(getActivity(),"tips_table",deletename,Login_username);
                        listItems.remove(poss);
                        simpleAdapter.notifyDataSetChanged();
                        updatelist();
                        Toast.makeText(context,deletename+"删除成功",Toast.LENGTH_SHORT).show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //通过消息的内容msg.what  分别更新ui
            switch (msg.what) {
                case MSG_ONE:
                    //获取到系统当前时间 long类型
                    long time = System.currentTimeMillis();
                    //将long类型的时间转换成日历格式
                    Date data = new Date(time);
                    // 转换格式，年月日时分秒 星期  的格式
                    SimpleDateFormat sdfd = new SimpleDateFormat("MM月dd日");
                    SimpleDateFormat sdft = new SimpleDateFormat("HH时mm分");
                    String s=(sdft.format(data)).substring(0,2);
                    int I = Integer.parseInt(s);
                    if (I>=6&&I<14){
                        time_type.setText("阳光正好...");
                    }
                    if (I>=14&&I<19){
                        time_type.setText("微风习习...");
                    }
                    if (I<6||I>=19){
                        time_type.setText("月色皎洁...");
                    }
                    //显示在textview上，通过转换格式
                    minute.setText(sdft.format(data));
                    day.setText(sdfd.format(data));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.note_add:
                Intent intent3 = new Intent(context,NoteActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            // do-while  一 什么什么 就
            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1000);
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = MSG_ONE;
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}