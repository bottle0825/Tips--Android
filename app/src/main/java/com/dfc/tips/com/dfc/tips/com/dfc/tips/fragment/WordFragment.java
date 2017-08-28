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
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dfc.tips.R;
import com.dfc.tips.com.dfc.tips.db.MyDB;

public class WordFragment extends Fragment implements View.OnClickListener{


    View view;

    private Context context;
    private MyDB myDB = new MyDB();
    WordWordFragment wwf;
    private LinearLayout wordnote_ll_word;
    private LinearLayout wordnote_ll_sentence;
    private WordWordFragment wordWordFragment;
    private WordSentenceFragment wordSentenceFragment;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void init(){
        wordnote_ll_word = (LinearLayout) view.findViewById(R.id.wordnote_ll_word);
        wordnote_ll_word.setOnClickListener(this);
        wordnote_ll_sentence = (LinearLayout) view.findViewById(R.id.wordnote_ll_sentence);
        wordnote_ll_sentence.setOnClickListener(this);
        setDefaultFragment();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void resetbutton(){
        wordnote_ll_word.setBackground(getResources().getDrawable(R.drawable.layout_border));
        wordnote_ll_sentence.setBackground(getResources().getDrawable(R.drawable.layout_border));
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
        view = inflater.inflate(R.layout.fragment_word, container, false);
//        Toast.makeText(context,"单词本打开了",Toast.LENGTH_SHORT).show();
        init();


        return view;
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        wwf = new WordWordFragment();
        resetbutton();
        wordnote_ll_word.setBackground(getResources().getDrawable(R.drawable.layout_border3));
        transaction.replace(R.id.wordnote_content, wwf);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        //开启Fragment事务
        android.app.FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()){
            case R.id.wordnote_ll_word:
//                Toast.makeText(context,"点击单词",Toast.LENGTH_SHORT).show();
                if (wordWordFragment == null) {
                    wordWordFragment = new WordWordFragment();
                }
                resetbutton();
                wordnote_ll_word.setBackground(getResources().getDrawable(R.drawable.layout_border3));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.wordnote_content, wordWordFragment);
                transaction.commit();
                break;
            case R.id.wordnote_ll_sentence:
//                Toast.makeText(context,"点击句子",Toast.LENGTH_SHORT).show();
                if (wordSentenceFragment == null) {
                    wordSentenceFragment = new WordSentenceFragment();
                }
                resetbutton();
                wordnote_ll_sentence.setBackground(getResources().getDrawable(R.drawable.layout_border3));
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.wordnote_content, wordSentenceFragment);
                transaction.commit();
                break;
        }
    }
}