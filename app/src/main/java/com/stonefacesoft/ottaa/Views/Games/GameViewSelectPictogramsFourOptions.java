package com.stonefacesoft.ottaa.Views.Games;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stonefacesoft.ottaa.Interfaces.Lock_Unlocked_Pictograms;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GameControl;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFuntionGames;
import com.stonefacesoft.ottaa.utils.Audio.MediaPlayerAudio;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.utils.Ttsutils.UtilsGamesTTS;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

public class GameViewSelectPictogramsFourOptions extends AppCompatActivity implements View
        .OnClickListener, Toolbar.OnMenuItemClickListener, Lock_Unlocked_Pictograms, Make_Click_At_Time, View.OnTouchListener {

    protected static PictoView Opcion1;
    protected static PictoView Opcion2;
    protected static PictoView Opcion3;
    protected static PictoView Opcion4;

    protected Context context;

    protected ImageButton imageButton;

    protected AnalyticsFirebase analitycsFirebase;
    protected Button btnBarrido;

    protected MediaPlayerAudio mediaPlayer, music;
    protected ImageView mAnimationWin;

    protected PictoView Seleccion1;

    protected int PictoID;                // picto actual
    protected int mPositionPadre;                // picto actual

    protected textToSpeech myTTS;

    protected TextView mSeleccion;

    protected CustomToast dialogo;

    protected UtilsGamesTTS mUtilsTTS;

    protected SharedPreferences sharedPrefsDefault;

    protected BarridoPantalla barridoPantalla;

    protected Juego game;

    protected ScrollFuntionGames function_scroll;
    protected GameControl gameControl;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean status_bar = intent.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        PictoID = intent.getIntExtra("PictoID", 0);
        mPositionPadre = intent.getIntExtra("PositionPadre", 0);
        analitycsFirebase = new AnalyticsFirebase(this);
        setContentView(R.layout.activity_noti_games);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void lockPictogram(boolean isSpeaking) {

    }

    @Override
    public void unlockPictogram() {

    }

    @Override
    public void OnClickBarrido() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    protected void setToolbarName(Toolbar toolbar,int id){
        toolbar.setTitle(getResources().getString(id));
        setSupportActionBar(toolbar);
    }

    protected void initComponents(){
        mSeleccion = findViewById(R.id.SeleccioneEste);
        mAnimationWin = findViewById(R.id.ganarImagen);
        mAnimationWin.setImageAlpha(230);
        mAnimationWin.setVisibility(View.INVISIBLE);
        Opcion1 = findViewById(R.id.Option1);
        Opcion1.setOnClickListener(this);
        Opcion2 = findViewById(R.id.Option2);
        Opcion2.setOnClickListener(this);
        Opcion3 = findViewById(R.id.Option3);
        Opcion3.setOnClickListener(this);
        Opcion4 = findViewById(R.id.Option4);
        Opcion4.setOnClickListener(this);
        btnBarrido = findViewById(R.id.btnBarrido);
        btnBarrido.setOnTouchListener(this);
        btnBarrido.setOnClickListener(this);
        imageButton = findViewById(R.id.ttsJuego);
        imageButton.setOnClickListener(this);
        mediaPlayer = new MediaPlayerAudio(this);
        music = new MediaPlayerAudio(this);
        mediaPlayer.setVolumenAudio(0.15f);
        music.setVolumenAudio(0.05f);
        Seleccion1 = findViewById(R.id.Seleccion1);
        Seleccion1.goneCustomTexto();
        Seleccion1.setOnClickListener(this);
    }

    protected void initUtilsTTS(SharedPreferences sharedPreferences){
        if (mUtilsTTS == null) {
            mUtilsTTS = new UtilsGamesTTS(this, myTTS.getTTS(), dialogo, sharedPreferences, this);
        }
    }

    protected void setIcon(MenuItem item, boolean status, int dEnabled, int dDisabled) {
        if (status) {
            item.setIcon(getResources().getDrawable(dEnabled));
        } else {
            item.setIcon(getResources().getDrawable(dDisabled));
        }
    }

    protected  void setUpGame(int id,int parent){
        game=new Juego(this,id,parent);
        game.startUseTime();
    }

    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }

}
