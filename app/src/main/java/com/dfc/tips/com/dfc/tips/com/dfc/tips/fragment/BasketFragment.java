package com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.accounts_food;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.tips_basket;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.tips_note;

public class BasketFragment extends Fragment implements View.OnClickListener{


    View view;

    private Context context;
    private MyDB myDB = new MyDB();
    private TextView basket_count;
    private RelativeLayout clear;
    private RelativeLayout restore;
    private Global_variable mGlobal_variable;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public void init(){
        basket_count = (TextView) view.findViewById(R.id.basket_count);
        clear = (RelativeLayout) view.findViewById(R.id.basket_clear);
        clear.setOnClickListener(this);
        restore = (RelativeLayout) view.findViewById(R.id.basket_restore);
        restore.setOnClickListener(this);

        updatelist();
    }
    public void updatelist(){
        tips_basket = new ArrayList<>();
        myDB.readDB(getActivity(),"tips_basket",Login_username);
        basket_count.setText(""+tips_basket.size());

        createlist();
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_basket, container, false);

        init();
//        Toast.makeText(getActivity(),""+tips_basket.size(),Toast.LENGTH_LONG).show();
        return view;
    }

    public void createlist(){
        //创建一个List集合
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = tips_basket.size() - 1; i >= 0; i--) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("tips_day", tips_basket.get(i).getTips_createtime());
            listItem.put("tips_title", tips_basket.get(i).getTips_name());
            listItem.put("tips_content", tips_basket.get(i).getTips_content());
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
                openItem.setTitle("Restore");
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
                        // restore
                        int pos1 = position;
                        position = tips_basket.size() - position - 1;
                        String restorename = tips_basket.get(position).getTips_name();
                        myDB.insert(getActivity(),"tips_table",tips_basket.get(position));
                        myDB.deleteDB(getActivity(),"tips_basket",restorename,Login_username);
                        listItems.remove(pos1);
                        simpleAdapter.notifyDataSetChanged();
                        updatelist();
                        Toast.makeText(context,restorename+"还原成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // delete
                        int pos2 = position;
                        position = tips_basket.size() - position - 1;
                        String deletename = tips_basket.get(position).getTips_name();
                        myDB.deleteDB(getActivity(),"tips_basket",deletename,Login_username);
                        listItems.remove(pos2);
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

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.basket_clear:
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                final View adda = layoutInflater.inflate(R.layout.alert_basket_clear, null);
                new AlertDialog.Builder(context)
                        .setView(adda)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0;i<tips_basket.size();i++){
                                    myDB.deleteDB(getActivity(),"tips_basket",tips_basket.get(i).getTips_name(),Login_username);
                                }
                                updatelist();
                            }
                        })
                        .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.basket_restore:
                LayoutInflater layoutInflater2 = LayoutInflater.from(context);
                final View adda2 = layoutInflater2.inflate(R.layout.alert_basket_restore, null);
                new AlertDialog.Builder(context)
                        .setView(adda2)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0;i<tips_basket.size();i++){
                                    myDB.insert(getActivity(),"tips_table",tips_basket.get(i));
                                    myDB.deleteDB(getActivity(),"tips_basket",tips_basket.get(i).getTips_name(),Login_username);
                                }
                                updatelist();
                            }
                        })
                        .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
                break;
            default:
                break;
        }
    }
}