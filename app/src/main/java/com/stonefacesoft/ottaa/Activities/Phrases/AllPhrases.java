package com.stonefacesoft.ottaa.Activities.Phrases;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.preference.PreferenceManager;

import com.google.android.exoplayer2.transformer.Transformer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stonefacesoft.ottaa.CompartirArchivos;
import com.stonefacesoft.ottaa.Interfaces.AudioTransformationListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.AllPhrasesRecyclerView;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;
import com.stonefacesoft.ottaa.utils.Audio.FileEncoder;
import com.stonefacesoft.ottaa.utils.textToSpeech;

public class AllPhrases extends PhrasesView implements AudioTransformationListener {
    private AllPhrasesRecyclerView allPhrasesRecyclerView;
    private textToSpeech myTTS;
    private FloatingActionButton shareFloattingButton;

    private boolean shareAction;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        this.setTitle(getString(R.string.allphrases));
    }

    @Override
    public void initComponents() {
        super.initComponents();
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(this);
        shareAction = sharedPrefsDefault.getBoolean(getString(R.string.enable_share_phrases),false);
        allPhrasesRecyclerView = new AllPhrasesRecyclerView(this,firebaseUser.getmAuth());
        myTTS = textToSpeech.getInstance(this);
        btnEditar.setVisibility(View.INVISIBLE);
        shareFloattingButton = findViewById(R.id.floatting_button_share);
        shareFloattingButton.setOnClickListener(this::onClick);
        allPhrasesRecyclerView.setMyTTS(myTTS);
        allPhrasesRecyclerView.setOnClickListener();
        if(shareAction)
            shareFloattingButton.setVisibility(View.VISIBLE);
        else
            shareFloattingButton.setVisibility(View.GONE);
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
            case R.id.floatting_button_share:
                CompartirArchivos compartirArchivos = new CompartirArchivos(this, myTTS,this);
                allPhrasesRecyclerView.shareAudio(compartirArchivos);
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

    @Override
    public void startAudioTransformation(Transformer.Listener listener, String pathFile, String locationPath) {
        new FileEncoder(this).encodeAudioFile(listener,pathFile,locationPath);
    }
}
