package com.stonefacesoft.ottaa.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Edit_Picto_Visual;
import com.stonefacesoft.ottaa.GaleriaGrupos2;
import com.stonefacesoft.ottaa.LicenciaExpirada;
import com.stonefacesoft.ottaa.LoginActivity2;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.Grupo_Recycler_View_Game;
import com.stonefacesoft.ottaa.Viewpagers.viewpager_game_filter_view;
import com.stonefacesoft.ottaa.Views.GroupGalleryNavigator;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GaleriaGruposControls;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GaleriaPictosControls;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.constants.ConstantsGroupGalery;


public class Groups_TellStory extends GroupGalleryNavigator {

    protected Grupo_Recycler_View_Game recycler_view_grupo;
    protected viewpager_game_filter_view viewpager;
    private int boton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        showViewPager = sharedPrefsDefault.getBoolean("showViewPagerGame", false);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editButton.setVisibility(View.INVISIBLE);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent mainIntent = new Intent().setClass(
                            Groups_TellStory.this, LoginActivity2.class);
                    startActivity(mainIntent);
                    Toast.makeText(Groups_TellStory.this, R.string.expired_sesions, Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    //UID Usuario
                    uid = mAuth.getCurrentUser().getUid();
                }

            }
        };
        initViewPager();
    }

    @Override
    protected void initViewPager() {
        iniciarBarrido();
        crearRecyclerView();
        viewpager = new viewpager_game_filter_view(this,myTTS);
        viewpager.showViewPager(showViewPager);
        deviceControl=new GaleriaGruposControls(this);

    }

    @Override
    protected void crearRecyclerView() {
        recycler_view_grupo = new Grupo_Recycler_View_Game(this,mAuth);
        recycler_view_grupo.setMyTTS(myTTS);
        recycler_view_grupo.setSharedPrefsDefault(sharedPrefsDefault);
        recycler_view_grupo.showRecyclerView(showViewPager);

    }

    private void navigateButtonAction(boolean next,String actionName){
        if(showViewPager)
            viewpager.scrollPosition(next);
        else  if(recycler_view_grupo!=null&&!showViewPager)
            recycler_view_grupo.scrollTo(next);
    /*    if(barridoPantalla.isBarridoActivado()){
            analyticsFirebase.customEvents("Accessibility","Galeria Grupos",actionName);
        }*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.up_button:
                navigateButtonAction(false,"Previous Button");
                break;
            case R.id.down_button:
                navigateButtonAction(true,"Next Button" );
                break;
            case R.id.back_button:
            /*    if(barridoPantalla.isBarridoActivado()){
                    analyticsFirebase.customEvents("Accessibility","Galeria Grupos","Close Galery Groups");
                }*/
                onBackPressed();

                break;
            case R.id.edit_button:

                break;
            case R.id.btnTalk:
                if(showViewPager){
                   /* if(barridoPantalla.isBarridoActivado())
                        analyticsFirebase.customEvents("Accessibility","Galeria Grupos","Select Group");*/
                    viewpager.OnClickItem();
                }
                break;
            case R.id.btnBarrido:
                if (barridoPantalla.isBarridoActivado() && barridoPantalla.isAvanzarYAceptar()) {
                   // Log.d(TAG, "onClick() returned: Barrido Pantalla");
                } else if (barridoPantalla.isBarridoActivado() && !barridoPantalla.isAvanzarYAceptar()) {
                    int posicion = barridoPantalla.getPosicionBarrido();
                    if (posicion != -1)
                        barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).callOnClick();
                }
                break;
            default:

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return deviceControl.makeClick(event);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantsGroupGalery
                    .GALERIAPICTOS:
                returnData(data, IntentCode.TELL_A_STORY);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onBackPressed() {

        //Uso este boolean para prevenir que se haga atras antes que se carguen los pictos, de esta forma prevenimos que se reemplaze
        //el grupo por ningun valor y borre to do;

            Intent databack = new Intent();
            databack.putExtra("ID", 0);
            databack.putExtra("Boton", boton);
            setResult(3, databack);
            finish();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_galeria_pictos, menu);
            item = menu.findItem(R.id.vincular);
            item.setVisible(false);
         /*   item = menu.findItem(R.id.listo);
            item.setVisible(false);*/
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(false);
            menu.getItem(6).setVisible(false);
            item=menu.findItem(R.id.tipe_view);
            item.setVisible(true);


            if(!showViewPager)
                item.setIcon(R.drawable.ic_baseline_view_carousel_white_24);
            else
                item.setIcon(R.drawable.ic_baseline_apps_white_24);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*Obtenemos el id del item del menu para luego usarlo para hacer alguna accion*/
        int id = item.getItemId();

        switch (id) {
            case R.id.tipe_view:
                showViewPager=!showViewPager;
                if(!showViewPager)
                    item.setIcon(R.drawable.ic_baseline_view_carousel_white_24);
                else
                    item.setIcon(R.drawable.ic_baseline_apps_white_24);
                sharedPrefsDefault.edit().putBoolean("showViewPagerGame",showViewPager).apply();
                viewpager.showViewPager(showViewPager);
                recycler_view_grupo.showRecyclerView(showViewPager);
                // showView(editButton,showViewPager);
                break;
        }

        return true;
    }


}
