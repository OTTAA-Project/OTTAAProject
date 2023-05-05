package com.stonefacesoft.ottaa.Activities.Phrases;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.Favorite_Phrases_recycler_view;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.SayActivityName;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import java.util.ArrayList;

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
        favorite_phrases_recycler_view.setMyTTS(textToSpeech.getInstance(this));
        if(favorite_phrases_recycler_view.getArray().length()==0){
            favorite_phrases_recycler_view.talkAtPosition();
        }
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
            startActivityForResult(new Intent(this,MostUsedPhrases.class), IntentCode.GALERIA_GRUPOS.getCode());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG, "onActivityResult: Entering ActivityResult");
        if(resultCode == IntentCode.VINCULAR_FRASES.getCode()){
            Bundle bundle = data.getExtras();
            int value = bundle.getInt("updateView",0);
            if(value ==1){
                favorite_phrases_recycler_view.updateData();
            }
        }
    }

    @Override
    protected void iniciarBarrido() {
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        listadoObjetosBarrido.add(previous);
        listadoObjetosBarrido.add(exit);
        listadoObjetosBarrido.add(btnTalk);
        listadoObjetosBarrido.add(foward);
        barridoPantalla = new BarridoPantalla(this, listadoObjetosBarrido);
        if (barridoPantalla.isBarridoActivado() && barridoPantalla.devolverpago()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Stuff that updates the UI
                    btnBarrido.setVisibility(View.VISIBLE);
                    if(barridoPantalla.isBarridoActivado())
                        barridoPantalla.changeButtonVisibility();
                }
            });
        }else{
            btnBarrido.setVisibility(View.GONE);
        }
    }
}
