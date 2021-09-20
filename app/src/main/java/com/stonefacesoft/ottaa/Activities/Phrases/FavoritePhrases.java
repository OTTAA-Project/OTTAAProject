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
import com.stonefacesoft.ottaa.RecyclerViews.Favorite_Phrases_recycler_view;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;
import com.stonefacesoft.ottaa.utils.Accesibilidad.SayActivityName;
import com.stonefacesoft.ottaa.utils.IntentCode;

public class FavoritePhrases extends PhrasesView {

    private Favorite_Phrases_recycler_view favorite_phrases_recycler_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Override
    public void initComponents() {
        super.initComponents();
        favorite_phrases_recycler_view = new Favorite_Phrases_recycler_view(this,firebaseUser.getmAuth());
        this.setTitle(getResources().getString(R.string.favorite_phrases));
        if(barridoPantalla.isBarridoActivado())
            favorite_phrases_recycler_view.setScrollVertical(false);
  //      most_used_recycler_view = new MostUsedPhrases_Recycler_View(this,firebaseUser.getmAuth());

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.up_button:
                mAnalyticsFirebase.customEvents("Touch","FavoritePhrases","Favorite Phrases UpButton");
                favorite_phrases_recycler_view.scrollTo(false);
                break;
            case R.id.down_button:
                mAnalyticsFirebase.customEvents("Touch","FavoritePhrases","Favorite Phrases DownButton");
                favorite_phrases_recycler_view.scrollTo(true);
                break;
            case R.id.back_button:
                mAnalyticsFirebase.customEvents("Touch","FavoritePhrases","Favorite Phrases BackButton");
                onBackPressed();
                break;
            case R.id.edit_button:
                mAnalyticsFirebase.customEvents("Touch","FavoritePhrases","Favorite Phrases EditButton");
                Intent intent=new Intent(this, VincularFrases.class);
                startActivityForResult(intent, IntentCode.CUSTOMPHRASES.getCode());
                break;
            case R.id.btnTalk:
                mAnalyticsFirebase.customEvents("Touch","FavoritePhrases","Favorite Phrases TalkAction");
                favorite_phrases_recycler_view.talkAtPosition();
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
        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        menuItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.vincular){
            SharedPreferences defaultSharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
            SayActivityName.getInstance(this).sayTitle(getResources().getString(R.string.frases_musadas));
            defaultSharedPreferences.edit().putInt("favoritePhrase",0).apply();
            startActivity(new Intent(this,MostUsedPhrases.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
