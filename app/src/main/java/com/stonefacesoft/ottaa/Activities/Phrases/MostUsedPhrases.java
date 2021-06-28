package com.stonefacesoft.ottaa.Activities.Phrases;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.MostUsedPhrases_Recycler_View;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;
import com.stonefacesoft.ottaa.utils.textToSpeech;

public class MostUsedPhrases extends PhrasesView {

    private MostUsedPhrases_Recycler_View most_used_recycler_view;
    private textToSpeech myTTS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Override
    public void initComponents() {
        super.initComponents();
        most_used_recycler_view = new MostUsedPhrases_Recycler_View(this, firebaseUser.getmAuth());
        myTTS = new textToSpeech(this);
        most_used_recycler_view.setMyTTS(myTTS);
        most_used_recycler_view.setOnClickListener();
        if(barridoPantalla.isBarridoActivado())
            most_used_recycler_view.setScrollVertical(false);
        //      most_used_recycler_view = new MostUsedPhrases_Recycler_View(this,firebaseUser.getmAuth());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.up_button:
                most_used_recycler_view.scrollTo(false);
                break;
            case R.id.down_button:
                most_used_recycler_view.scrollTo(true);
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.edit_button:

                break;
            case R.id.btnTalk:
                most_used_recycler_view.getmRecyclerView().callOnClick();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
