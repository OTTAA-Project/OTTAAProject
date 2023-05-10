package com.stonefacesoft.ottaa.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;

import com.stonefacesoft.ottaa.GaleriaPictos3;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Viewpagers.viewpager_galeria_pictos;
import com.stonefacesoft.ottaa.utils.textToSpeech;

public class PictogramsGalleryGames extends GaleriaPictos3 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_galeria_pictos, menu);
        this.menu = menu;
        MenuItem item;
        item = menu.findItem(R.id.tipe_view);
        if (!showViewPager)
            item.setIcon(R.drawable.ic_baseline_view_carousel_white_24);
        else
            item.setIcon(R.drawable.ic_baseline_apps_white_24);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);
        menu.getItem(5).setVisible(false);
        menu.getItem(6).setVisible(false);
        return true;
    }

    @Override
    protected void setUpViewPager() {
        myTTS = textToSpeech.getInstance(this);
        viewpager_galeria_pictos = new viewpager_galeria_pictos(this, myTTS, boton);
        Bundle extras = getIntent().getExtras();
        showViewPager = sharedPrefsDefault.getBoolean("games_viewpager", false);
        if (extras != null) {
            esVincular = extras.getBoolean("esVincular", false);
            isSorter = extras.getBoolean("esOrdenar", false);
            boton = extras.getInt("Boton", 0);
            nombre = extras.getString("Nombre");
            if (esVincular || isSorter )
                showViewPager = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()){
             case R.id.tipe_view:
                 showViewPager = !showViewPager;
                 if (!showViewPager)
                     item.setIcon(R.drawable.ic_baseline_view_carousel_white_24);
                 else
                     item.setIcon(R.drawable.ic_baseline_apps_white_24);
                 sharedPrefsDefault.edit().putBoolean("games_viewpager", showViewPager).apply();
                 viewpager_galeria_pictos.showViewPager(showViewPager);
                 if (mPictoRecyclerView != null)
                     mPictoRecyclerView.showRecyclerView(showViewPager);
                 return true;
         }
        return super.onOptionsItemSelected(item);
    }
}
