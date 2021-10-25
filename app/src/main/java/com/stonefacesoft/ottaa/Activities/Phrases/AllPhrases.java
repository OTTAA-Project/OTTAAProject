package com.stonefacesoft.ottaa.Activities.Phrases;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.AllPhrasesRecyclerView;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;
import com.stonefacesoft.ottaa.utils.textToSpeech;

public class AllPhrases extends PhrasesView {
    private AllPhrasesRecyclerView allPhrasesRecyclerView;
    private textToSpeech myTTS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        this.setTitle(getString(R.string.allphrases));
    }

    @Override
    public void initComponents() {
        super.initComponents();
        allPhrasesRecyclerView = new AllPhrasesRecyclerView(this,firebaseUser.getmAuth());
        myTTS = textToSpeech.getInstance(this);
        btnEditar.setVisibility(View.INVISIBLE);
        allPhrasesRecyclerView.setMyTTS(myTTS);
        allPhrasesRecyclerView.setOnClickListener();
        if(barridoPantalla.isBarridoActivado())
            allPhrasesRecyclerView.setScrollVertical(false);
        TAG ="AllPhrases";
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.up_button:
                mAnalyticsFirebase.customEvents("Touch","AllPhrases","All Phrases UpButton");
                allPhrasesRecyclerView.scrollTo(false);
                break;
            case R.id.down_button:
                mAnalyticsFirebase.customEvents("Touch","AllPhrases","All Phrases DownButton");
                allPhrasesRecyclerView.scrollTo(true);
                break;
            case R.id.back_button:
                mAnalyticsFirebase.customEvents("Touch","AllPhrases","Favorite Phrases BackButton");
                onBackPressed();
                break;
            case R.id.edit_button:

                break;
            case R.id.btnTalk:
                mAnalyticsFirebase.customEvents("Touch","AllPhrases","All Phrases Talk action");
                allPhrasesRecyclerView.talkAtPosition();
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem menuItem =menu.findItem(R.id.action_search);
        menuItem.setVisible(true);
        if (menu.findItem(R.id.action_search).isVisible()) {
            setmSearchView((SearchView) menuItem.getActionView());
        }
        return true;
    }
    public void setmSearchView(SearchView searchView) {
            allPhrasesRecyclerView.setSearchView(searchView);
    }
}
