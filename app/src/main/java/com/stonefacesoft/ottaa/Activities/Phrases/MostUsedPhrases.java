package com.stonefacesoft.ottaa.Activities.Phrases;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.MostUsedPhrases_Recycler_View;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;
import com.stonefacesoft.ottaa.utils.Accesibilidad.SayActivityName;
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
        this.setTitle(getResources().getString(R.string.frases_musadas));
        most_used_recycler_view = new MostUsedPhrases_Recycler_View(this, firebaseUser.getmAuth());
        myTTS = textToSpeech.getInstance(this);
        most_used_recycler_view.setMyTTS(myTTS);
        most_used_recycler_view.setOnClickListener();
        btnEditar.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));
        if(barridoPantalla.isBarridoActivado())
            most_used_recycler_view.setScrollVertical(false);
        //      most_used_recycler_view = new MostUsedPhrases_Recycler_View(this,firebaseUser.getmAuth());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.up_button:
                mAnalyticsFirebase.customEvents("Touch","MostUsedPhrases","Most Used Phrases UpButton");
                most_used_recycler_view.scrollTo(false);
                break;
            case R.id.down_button:
                mAnalyticsFirebase.customEvents("Touch","MostUsedPhrases","Most Used Phrases DownButton");
                most_used_recycler_view.scrollTo(true);
                break;
            case R.id.back_button:
                mAnalyticsFirebase.customEvents("Touch","MostUsedPhrases","Most Used Phrases BackButton");
                onBackPressed();
                break;
            case R.id.edit_button:
                mAnalyticsFirebase.customEvents("Touch","MostUsedPhrases","All Phrases");
                SayActivityName.getInstance(this).sayTitle("Todas las frases");
                startActivity(new Intent(this,AllPhrases.class));
                break;
            case R.id.btnTalk:
                mAnalyticsFirebase.customEvents("Touch","MostUsedPhrases","Talk Action");
                if(most_used_recycler_view.getArray().length()>0)
                    most_used_recycler_view.talkAtPosition();
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
        MenuItem menuItem =menu.findItem(R.id.vincular);
        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_star_black_24dp));
        menuItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.vincular){
            SharedPreferences defaultSharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
            SayActivityName.getInstance(this).sayTitle(getResources().getString(R.string.favorite_phrases));
            defaultSharedPreferences.edit().putInt("favoritePhrase",1).apply();
            startActivity(new Intent(this,FavoritePhrases.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
