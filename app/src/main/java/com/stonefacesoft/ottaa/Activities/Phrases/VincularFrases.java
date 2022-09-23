package com.stonefacesoft.ottaa.Activities.Phrases;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.PhrasesRecyclerView;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.IntentCode;

public class VincularFrases extends PhrasesView {

    private PhrasesRecyclerView recyclerView;
    private SubirArchivosFirebase subirArchivos;
    private int fileIsSaved = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Override
    public void initComponents(){
        super.initComponents();
        Intent intent = getIntent();
        this.setTitle(getString(R.string.selectPhavoritePhrases));
        subirArchivos=new SubirArchivosFirebase(this);
        recyclerView=new PhrasesRecyclerView(this,firebaseUser.getmAuth());
        btnEditar.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_save_white_24));
        if(barridoPantalla.isBarridoActivado())
            recyclerView.setScrollVertical(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_button:
                mAnalyticsFirebase.customEvents("Touch","VincularFrases","Favorite Phrases UpButton");
                recyclerView.scrollTo(false);
                break;
            case R.id.down_button:
                mAnalyticsFirebase.customEvents("Touch","VincularFrases","Favorite Phrases DownButton");
                recyclerView.scrollTo(true);
                break;
            case R.id.back_button:
                mAnalyticsFirebase.customEvents("Touch","VincularFrases","Favorite Phrases BackButton");
                onBackPressed();
                break;
            case R.id.edit_button:
                mAnalyticsFirebase.customEvents("Touch","VincularFrases","SaveFavoritePhrases");
                recyclerView.savePhrases();
                subirArchivos.uploadFavoritePhrases(subirArchivos.getmDatabase(firebaseUser.getmAuth(), Constants.FrasesFavoritas),subirArchivos.getmStorageRef(firebaseUser.getmAuth(),Constants.FrasesFavoritas));
                fileIsSaved = 1;
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed: Volviendooo" );
        Intent databack = new Intent();
        databack.putExtra("updateView",fileIsSaved);
        setResult(IntentCode.VINCULAR_FRASES.getCode(), databack);
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
        recyclerView.setSearchView(searchView);
    }
}
