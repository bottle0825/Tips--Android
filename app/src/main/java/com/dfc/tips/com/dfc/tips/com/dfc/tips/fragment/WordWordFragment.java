package com.dfc.tips.com.dfc.tips.com.dfc.tips.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dfc.tips.R;
import com.dfc.tips.com.dfc.tips.db.MyDB;
import com.dfc.tips.com.dfc.tips.msg.Global_variable;
import com.dfc.tips.com.dfc.tips.msg.Words;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.words;

public class WordWordFragment extends Fragment implements View.OnClickListener{


    View view;

    private Context context;
    private MyDB myDB = new MyDB();
    private Words addWord = new Words();
    private LinearLayout add;
    private LinearLayout search;
    private EditText searchview;
    private EditText addWordE;
    private EditText addWordC;
    private EditText addWordM;
    private Global_variable mGlobal_variable;

    public void init() {
        add = (LinearLayout) view.findViewById(R.id.wordnote_word_addbutton);
        add.setOnClickListener(this);
        search = (LinearLayout) view.findViewById(R.id.wordnote_word_searchbutton);
        search.setOnClickListener(this);
        searchview = (EditText) view.findViewById(R.id.wordnote_word_searchview);
        words = new ArrayList<>();
        myDB.readDB(context,"wordnote_word",Login_username);
//        Toast.makeText(context,""+words.size(),Toast.LENGTH_SHORT).show();
        createList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_word_word, container, false);

        init();
        return view;
    }
    public void createList(){
        //创建一个List集合
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = words.size()-1; i >=0; i--) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("english", words.get(i).getEnglish());
            listItem.put("chinese", words.get(i).getChinese());
            listItem.put("message", words.get(i).getMessage());
            listItems.add(listItem);
        }
        //创建一个简单SimpleAdapter
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.wordlist,
                new String[]{"english", "chinese", "message"},
                new int[]{R.id.wordnote_wordlist_wordE, R.id.wordnote_wordlist_wordC, R.id.wordnote_wordlist_wordM}
        );
        final SwipeMenuListView list = (SwipeMenuListView) view.findViewById(R.id.wordnote_wordlist);
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
                openItem.setTitle("Modify");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "open" item
                SwipeMenuItem readItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                readItem.setBackground(new ColorDrawable(Color.rgb(0x48, 0xE4,
                        0x77)));
                // set item width
                readItem.setWidth(dip2px(getActivity(),90));
                // set item title
                readItem.setTitle("Skilled");
                // set item title fontsize
                readItem.setTitleSize(18);
                // set item title font color
                readItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(readItem);

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
            public boolean onMenuItemClick(final int myposition, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        final int mypos = words.size()-1;
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        final View adda = layoutInflater.inflate(R.layout.addalert, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(adda);
                        addWordE = (EditText) adda.findViewById(R.id.wordnote_addword_wordE);
                        addWordC = (EditText) adda.findViewById(R.id.wordnote_addword_wordC);
                        addWordM = (EditText) adda.findViewById(R.id.wordnote_addword_wordM);
                        addWordE.setText(words.get(mypos).getEnglish());
                        addWordC.setText(words.get(mypos).getChinese());
                        addWordM.setText(words.get(mypos).getMessage());
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addWord.setEnglish(addWordE.getText().toString());
                                addWord.setChinese(addWordC.getText().toString());
                                addWord.setMessage(addWordM.getText().toString());
                                addWord.setUsername(Login_username);
                                myDB.deleteDB(context,"wordnote_word",words.get(mypos).getEnglish(),Login_username);
                                myDB.insertword(context,addWord);
                                listItems.remove(myposition);
                                simpleAdapter.notifyDataSetChanged();
                                Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                                init();
                            }
                        }).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                        break;
                    case 1:
                        break;
                    case 2:
                        // delete
                        myDB.deleteDB(context,"wordnote_word",words.get(words.size()-myposition-1).getEnglish(),Login_username);
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


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.wordnote_word_addbutton:
                addalert();
                break;
            case R.id.wordnote_word_searchbutton:
                query();
                searchview.setText("");
                break;
        }
    }
    public void addalert(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View adda = layoutInflater.inflate(R.layout.addalert, null);
        new AlertDialog.Builder(context)
                .setView(adda)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addWordE = (EditText) adda.findViewById(R.id.wordnote_addword_wordE);
                        addWordC = (EditText) adda.findViewById(R.id.wordnote_addword_wordC);
                        addWordM = (EditText) adda.findViewById(R.id.wordnote_addword_wordM);
                        addWord.setEnglish(addWordE.getText().toString());
                        addWord.setChinese(addWordC.getText().toString());
                        addWord.setMessage(addWordM.getText().toString());
                        addWord.setUsername(Login_username);
                        myDB.insertword(context,addWord);
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
    public void query(){
        List<Words> qwords = new ArrayList<>();
        for (int i = 0;i<words.size();i++){
            int result = words.get(i).getEnglish().indexOf(searchview.getText().toString());
            if(result!=-1){
                qwords.add(words.get(i));
            }
        }
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = qwords.size()-1; i >=0; i--) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("english", qwords.get(i).getEnglish());
            listItem.put("chinese", qwords.get(i).getChinese());
            listItem.put("message", qwords.get(i).getMessage());
            listItems.add(listItem);
        }
        //创建一个简单SimpleAdapter
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.wordlist,
                new String[]{"english", "chinese", "message"},
                new int[]{R.id.wordnote_wordlist_wordE, R.id.wordnote_wordlist_wordC, R.id.wordnote_wordlist_wordM}
        );
        final SwipeMenuListView list = (SwipeMenuListView) view.findViewById(R.id.wordnote_wordlist);
        list.setAdapter(simpleAdapter);
    }
}