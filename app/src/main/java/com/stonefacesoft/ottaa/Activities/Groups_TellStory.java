package com.stonefacesoft.ottaa.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.LoginActivity2;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.Grupo_Recycler_View_Game;
import com.stonefacesoft.ottaa.Viewpagers.viewpager_game_filter_view;
import com.stonefacesoft.ottaa.Views.GroupGalleryNavigator;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GaleriaGruposControls;


public class Groups_TellStory extends GroupGalleryNavigator {

    private Grupo_Recycler_View_Game recycler_view_grupo;
    private viewpager_game_filter_view viewpager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        showViewPager = sharedPrefsDefault.getBoolean("showViewPager", false);
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
        iniciarBarrido();
        initViewPager();
    }

    @Override
    protected void initViewPager() {
        crearRecyclerView();
        viewpager = new viewpager_game_filter_view(this,myTTS);
        viewpager.showViewPager(showViewPager);
        deviceControl=new GaleriaGruposControls(this);

    }

    @Override
    protected void crearRecyclerView() {
        recycler_view_grupo = new Grupo_Recycler_View_Game(this,mAuth);
        recycler_view_grupo.setMyTTS(myTTS);
        recycler_view_grupo.cargarGrupo();
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

}
