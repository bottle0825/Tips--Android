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
import com.dfc.tips.com.dfc.tips.msg.Sentences;
import com.dfc.tips.com.dfc.tips.msg.Words;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dfc.tips.com.dfc.tips.msg.Tips_note.Login_username;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.sentences;
import static com.dfc.tips.com.dfc.tips.msg.Tips_note.words;

public class WordSentenceFragment extends Fragment implements View.OnClickListener{


    View view;

    private Context context;
    private MyDB myDB = new MyDB();
    private Sentences addSentence = new Sentences();
    private LinearLayout add;
    private LinearLayout search;
    private EditText searchview;
    private EditText addsentenceE;
    private EditText addsentenceC;
    private EditText addsentenceM;
    private Global_variable mGlobal_variable;

    public void init() {
        add = (LinearLayout) view.findViewById(R.id.wordnote_sentence_addbutton);
        add.setOnClickListener(this);
        search = (LinearLayout) view.findViewById(R.id.wordnote_sentence_searchbutton);
        search.setOnClickListener(this);
        searchview = (EditText) view.findViewById(R.id.wordnote_sentence_searchview);
        sentences = new ArrayList<>();
        myDB.readDB(context,"wordnote_sentence",Login_username);
//        Toast.makeText(context,""+sentences.size(),Toast.LENGTH_SHORT).show();
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
        view = inflater.inflate(R.layout.fragment_word_sentence, container, false);

        init();
        return view;
    }
    public void createList(){
        //创建一个List集合
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = sentences.size()-1; i >=0; i--) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("english", sentences.get(i).getEnglish());
            listItem.put("chinese", sentences.get(i).getChinese());
            listItem.put("message", sentences.get(i).getMessage());
            listItems.add(listItem);
        }
        //创建一个简单SimpleAdapter
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.sentencelist,
                new String[]{"english", "chinese", "message"},
                new int[]{R.id.wordnote_sentencelist_sentenceE, R.id.wordnote_sentencelist_sentenceC, R.id.wordnote_sentencelist_sentenceM}
        );
        final SwipeMenuListView list = (SwipeMenuListView) view.findViewById(R.id.wordnote_sentencelist);
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
                        final int mypos = sentences.size()-1;
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        final View adda = layoutInflater.inflate(R.layout.addalertsen, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(adda);
                        addsentenceE = (EditText) adda.findViewById(R.id.wordnote_addword_sentenceE);
                        addsentenceC = (EditText) adda.findViewById(R.id.wordnote_addword_sentenceC);
                        addsentenceM = (EditText) adda.findViewById(R.id.wordnote_addword_sentenceM);
                        addsentenceE.setText(sentences.get(mypos).getEnglish());
                        addsentenceC.setText(sentences.get(mypos).getChinese());
                        addsentenceM.setText(sentences.get(mypos).getMessage());
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addSentence.setEnglish(addsentenceE.getText().toString());
                                addSentence.setChinese(addsentenceC.getText().toString());
                                addSentence.setMessage(addsentenceM.getText().toString());
                                addSentence.setUsername(Login_username);
                                myDB.deleteDB(context,"wordnote_sentence",sentences.get(mypos).getEnglish(),Login_username);
                                myDB.insertsentence(context,addSentence);
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
                        // delete
                        myDB.deleteDB(context,"wordnote_sentence",sentences.get(sentences.size()-myposition-1).getEnglish(),Login_username);
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
            case R.id.wordnote_sentence_addbutton:
                addalert();
                break;
            case R.id.wordnote_sentence_searchbutton:
                query();
                searchview.setText("");
                break;
        }
    }
    public void addalert(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View adda = layoutInflater.inflate(R.layout.addalertsen, null);
        new AlertDialog.Builder(context)
                .setView(adda)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addsentenceE = (EditText) adda.findViewById(R.id.wordnote_addword_sentenceE);
                        addsentenceC = (EditText) adda.findViewById(R.id.wordnote_addword_sentenceC);
                        addsentenceM = (EditText) adda.findViewById(R.id.wordnote_addword_sentenceM);
                        addSentence.setEnglish(addsentenceE.getText().toString());
                        addSentence.setChinese(addsentenceC.getText().toString());
                        addSentence.setMessage(addsentenceM.getText().toString());
                        addSentence.setUsername(Login_username);
                        myDB.insertsentence(context,addSentence);
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
        List<Sentences> qsentences = new ArrayList<>();
        for (int i = 0;i<sentences.size();i++){
            int result = sentences.get(i).getEnglish().indexOf(searchview.getText().toString());
            if(result!=-1){
                qsentences.add(sentences.get(i));
            }
        }
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = qsentences.size()-1; i >=0; i--) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("english", qsentences.get(i).getEnglish());
            listItem.put("chinese", qsentences.get(i).getChinese());
            listItem.put("message", qsentences.get(i).getMessage());
            listItems.add(listItem);
        }
        //创建一个简单SimpleAdapter
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.sentencelist,
                new String[]{"english", "chinese", "message"},
                new int[]{R.id.wordnote_sentencelist_sentenceE, R.id.wordnote_sentencelist_sentenceC, R.id.wordnote_sentencelist_sentenceE}
        );
        final SwipeMenuListView list = (SwipeMenuListView) view.findViewById(R.id.wordnote_sentencelist);
        list.setAdapter(simpleAdapter);
    }
}