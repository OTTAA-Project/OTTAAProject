package com.stonefacesoft.ottaa.Views.Games;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.InputDevice;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GameControl;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFuntionGames;
import com.stonefacesoft.ottaa.utils.Audio.MediaPlayerAudio;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.GamesSettings;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameViewSelectPictograms extends AppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener, Make_Click_At_Time,View.OnTouchListener {
    protected PictoView opcion1;
    protected PictoView opcion2;
    protected PictoView opcion3;
    protected PictoView opcion4;
    protected PictoView lastPictogram;

    protected final String TAG="GameViewSelectPictogram";

    protected PictoView guess1;
    protected PictoView guess2;
    protected PictoView guess3;
    protected PictoView guess4;
    protected PictoView lastButton;
    protected PictoView animarPicto;
    protected ScrollFuntionGames function_scroll;

    protected SharedPreferences sharedPrefsDefault;
    protected CustomToast dialogo;
    //Declaracion de variables del TTS
    protected textToSpeech mTTS;
    protected int PictoID;
    protected int mPositionPadre;


    protected Json json;
    protected JSONArray hijos;
    protected JSONObject[] pictogramas;
    protected JSONArray mjJsonArrayTodosLosGrupos;
    protected ArrayList<Integer> numeros;


    protected MediaPlayerAudio player;
    protected MediaPlayerAudio music;

    protected String name;




    protected boolean useHappySound;



    protected int lastPosicion;

    protected Toolbar toolbar;
    protected Menu mMenu;

    protected Juego game;

    protected AnimGameScore animGameScore;
    protected ImageView mAnimationWin;
    protected ImageView mAnimationWin2;

    protected Handler handlerHablar;
    protected AnalyticsFirebase analyticsFirebase;
    protected BarridoPantalla barridoPantalla;
    protected Button btnBarrido;
    protected GameControl gameControl;
    protected Intent intent;
    protected MenuItem scoreItem;
    protected GamesSettings gamesSettings;





    protected final Runnable animarHablar = new Runnable() {
        @Override
        public void run() {
            if(lastButton!=null)
                animateGanador(lastButton,1);
            else if(lastPictogram!=null)
                animateGanador(lastPictogram,0);
            if (sharedPrefsDefault.getBoolean(getString(R.string.str_pistas), false))
                handlerHablar.postDelayed(this, 4000);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analyticsFirebase=new AnalyticsFirebase(this);
        intent = getIntent();
        boolean status_bar = intent.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.games_seleccionar_picto);
        dialogo=CustomToast.getInstance(this);
        PictoID = intent.getIntExtra("PictoID", 0);
        mPositionPadre = intent.getIntExtra("PositionPadre", 0);
        gamesSettings = new GamesSettings();
        json = Json.getInstance();
        json.setmContext(this);
        json.initSharedPrefs();
        toolbar=findViewById(R.id.toolbar);
        mjJsonArrayTodosLosGrupos=json.getmJSONArrayTodosLosGrupos();
        setSupportActionBar(toolbar);
        iniciarComponentes();
        handlerHablar=new Handler();
        hijos=json.getHijosGrupo2(mPositionPadre);
        numeros=new ArrayList<>();
        animGameScore = new AnimGameScore(this, mAnimationWin);
    }

    /**
     *  This method selects the pictograms when the game starts
     * */
    protected void selectRandomOptions(){

    }


    @Override
    public void onClick(View view) {

    }

    /**
     * This method enables the pictograms in a determined moment
     * */
    protected void enablePictogram(PictoView picto, boolean isButton) {
        picto.setEnabled(true);
        picto.setAlpha(1f);
        picto.setVisibility(View.VISIBLE);
        animatePictoAndReset(picto);
        if(isButton)
            picto.setInvisibleCustomTexto();
    }

    /**
     * This method enable or disable a button for a few time
     * */
    public void habilitarDesHabilitarBotones(PictoView button){
        button.setEnabled(!button.isEnabled());
    }

    protected void cargarPictogramas(){

    }

    protected  void setUpGame(int id,int parent){
            game=new Juego(this,id,parent);
            game.startUseTime();
    }

    protected void iniciarComponentes(){
        //inicio los componentes
        opcion1 = findViewById(R.id.Option1);
        opcion2 = findViewById(R.id.Option2);
        opcion3 = findViewById(R.id.Option3);
        opcion4 = findViewById(R.id.Option4);
        guess1 = findViewById(R.id.Guess1);
        guess2 = findViewById(R.id.Guess2);
        guess3 = findViewById(R.id.Guess3);
        guess4 = findViewById(R.id.Guess4);
        btnBarrido=findViewById(R.id.btnBarrido);
        btnBarrido.setOnTouchListener(this);
        btnBarrido.setOnClickListener(this);
        opcion1.setOnClickListener(this);
        opcion2.setOnClickListener(this);
        opcion3.setOnClickListener(this);
        opcion4.setOnClickListener(this);
        guess1.setOnClickListener(this);
        guess2.setOnClickListener(this);
        guess3.setOnClickListener(this);
        guess4.setOnClickListener(this);
        dialogo=CustomToast.getInstance(this);
        pictogramas=new JSONObject[4];
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(this);
        gamesSettings.enableSound(sharedPrefsDefault.getBoolean("muteSound",false));
        gamesSettings.enableRepeatFunction(sharedPrefsDefault.getBoolean("repetir",false));
//        if(mute)
//            sound_on_off.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_white_24dp));
//        else
//            sound_on_off.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
        gamesSettings.enableHelpFunction(sharedPrefsDefault.getBoolean(getString(R.string.str_pistas),true));
        mTTS = textToSpeech.getInstance(this);
        player=new MediaPlayerAudio(this);
        music=new MediaPlayerAudio(this);
        player.setVolumenAudio(0.15f);
        music.setVolumenAudio(0.05f);
        music.setMuted(gamesSettings.isSoundOn());
        music.playMusic();

        mAnimationWin=setUpAnimationWin(R.id.ganarImagen);
        mAnimationWin2=setUpAnimationWin(R.id.ganarImagen2);
        iniciarBarrido();
        function_scroll=new ScrollFuntionGames(this);
        gameControl=new GameControl(this);
    }

    protected void showDescription(String description){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTTS.getUtilsTTS().hablarConDialogo(description);
            }
        },800);
    }

    /**
     * This method select a random pictogram
     * */
    protected void selectRandomPictogram(int pos) {

    }

    protected void cargarOpcion(int pos){

    }

    protected void cargarValores(int pos) {

    }

    protected void decirPictoAleatorio(){

    }

    protected void cargarTextoBoton(double valor,int pos){

    }

    protected void isCorrect(PictoView view){

    }

    protected void WrongAction(){

    }

    protected void WrongAction(boolean isPictogram){

    }

    protected void CorrectAction(){

    }


    protected void esCorrecto(boolean esPicto) {

    }

    protected boolean verificarSiHayQueHacerReinicio(){
        return false;
    }




    public void reset(){

    }

    protected void lockPictogramOption(int opc, PictoView btn){

    }

    protected void makeClickOption(boolean esPicto){
    }

    protected void speakOption(PictoView option){
        player.pauseAudio();
    }
    protected void restartLection(){

    }

    protected void cargarPuntos(){
        game.getScoreClass().getResult();
        drawImageAtPosition();
    }

    //Anima el picto correctamente seleccionado
    protected void animateWinnerPictogram(PictoView from, PictoView to) {

    }

    protected void animatePictoAndReset(PictoView picto) {

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0);
        animation.setRepeatMode(0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        picto.startAnimation(animation);

    }


    protected void selectRandomSoundWin() {
        player.playYesSound();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //la variable temporal galeria grupos se la usa para modificar puntaje
    }

    protected int GetPosicionLastButton(){
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_game, menu);
        mMenu=menu;
        scoreItem=menu.findItem(R.id.score);
        setMenuScoreIcon();
        menu.getItem(2).setOnMenuItemClickListener(this);
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setOnMenuItemClickListener(this);
        menu.getItem(1).setVisible(true);
        menu.getItem(0).setOnMenuItemClickListener(this);
        menu.getItem(3).setOnMenuItemClickListener(this);

        setIcon(mMenu.getItem(3), gamesSettings.isSoundOn(),R.drawable.ic_volume_off_white_24dp,R.drawable.ic_volume_up_white_24dp);
        setIcon(mMenu.getItem(1), gamesSettings.isHelpFunction(),R.drawable.ic_live_help_white_24dp,R.drawable.ic_unhelp);
        setIcon(mMenu.getItem(2), gamesSettings.isRepeat(),R.drawable.ic_repeat_white_24dp,R.drawable.ic_unrepeat_ic_2);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
     return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(music!=null){
            music.stop();
            music.playMusic();
        }
        Json.getInstance().setmContext(this);
    }

    protected void drawImageAtPosition(){
        Drawable drawable=game.getSmiley();
        drawable.setTint(getResources().getColor(R.color.colorWhite));
        mMenu.getItem(0).setIcon(drawable);
    }


    protected void animateGanador(PictoView picto_ganador,int tipo){
        switch (tipo){
            case 0:
                selectButtonGanador(picto_ganador.getCustom_Texto()).startAnimation(AnimationUtils.loadAnimation(GameViewSelectPictograms.this, R.anim.shake));
                break;
            case 1:
                selectImagenGanadora(picto_ganador.getCustom_Texto()).startAnimation(AnimationUtils.loadAnimation(GameViewSelectPictograms.this, R.anim.shake));
                break;
        }
    }

    protected PictoView selectButtonGanador(String text){
        if(guess1.getCustom_Texto().equals(text))
            return guess1;
        else if(guess2.getCustom_Texto().equals(text))
            return guess2;
        else if(guess3.getCustom_Texto().equals(text))
            return guess3;
        else
            return guess4;

    }

    protected PictoView selectImagenGanadora(String text){
        if(opcion1.getCustom_Texto().equals(text))
            return opcion1;
        else if(opcion2.getCustom_Texto().equals(text))
            return opcion2;
        else if(opcion3.getCustom_Texto().equals(text))
            return opcion3;
        else
            return opcion4;

    }

    protected void setIcon(MenuItem item,boolean status,int dEnabled,int dDisabled){

        if (status) {
            item.setIcon(getResources().getDrawable(dEnabled));
        } else {
            item.setIcon(getResources().getDrawable(dDisabled));
        }
    }

    protected void iniciarBarrido() {
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        listadoObjetosBarrido.add(opcion1);
        listadoObjetosBarrido.add(opcion2);
        listadoObjetosBarrido.add(opcion3);
        listadoObjetosBarrido.add(opcion4);
        listadoObjetosBarrido.add(guess1);
        listadoObjetosBarrido.add(guess2);
        listadoObjetosBarrido.add(guess3);
        listadoObjetosBarrido.add(guess4);
        //  listadoObjetosBarrido.add(editButton);
        barridoPantalla = new BarridoPantalla(this, listadoObjetosBarrido);
        if (barridoPantalla.isBarridoActivado() && barridoPantalla.devolverpago()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Stuff that updates the UI
                    btnBarrido.setVisibility(View.VISIBLE);
                }
            });
        }else{
            btnBarrido.setVisibility(View.GONE);
        }
    }


    @Override
    public void OnClickBarrido() {
        if(function_scroll.isClickEnabled()&&barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).getId()==R.id.btnTodosLosPictos)
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        else if(!function_scroll.isClickEnabled()){
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gameControl.makeClick(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameControl.makeClick(event);

    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_SCROLL:

                    if(barridoPantalla.isScrollMode()||barridoPantalla.isScrollModeClicker()){
                        if(event.getAxisValue(MotionEvent.AXIS_VSCROLL)<0.0f){
                            if(barridoPantalla.isScrollMode())
                                function_scroll.HacerClickEnTiempo();
                            barridoPantalla.avanzarBarrido();
                        }
                        else{
                            if(barridoPantalla.isScrollMode())
                                function_scroll.HacerClickEnTiempo();
                            barridoPantalla.volverAtrasBarrido();

                        }
                    }
                    return true;
            }
        }
        return super.onGenericMotionEvent(event);    }

    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }
    protected void setGuessDrawable(PictoView view){
        Drawable drawable=getDrawable(R.drawable.ic_help_outline_black_24dp);
        if(ValidateContext.isValidContext(this))
            new GlideAttatcher(this).setScale(4).UseCornerRadius(true).loadDrawable(drawable,view.getImageView());
    }

    protected void setVisibleText(PictoView view){
        view.setVisibleText();
    }
    protected void setInvisibleText(PictoView view){
        view.setInvisibleCustomTexto();
    }

    protected void lockOptions(){}
    protected void unlockOptions(){}

    @Override
    public void onTrimMemory(int level) {
        switch (level) {
            case GameViewSelectPictograms.TRIM_MEMORY_BACKGROUND:
                break;
            case GameViewSelectPictograms.TRIM_MEMORY_MODERATE:
                break;
            case GameViewSelectPictograms.TRIM_MEMORY_RUNNING_CRITICAL:
                break;
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.onTrimMemory(TRIM_MEMORY_RUNNING_LOW);
        resetAudio(player);
        resetAudio(music);
        Log.d(TAG, "onLowMemory: Trimming Memory");
    }

    public void resetAudio(MediaPlayerAudio audio){
        if(audio!= null)
            audio.reset();
    }

    public void playCorrectSound(){
        int i = (int) (Math.random() * 4);
        Log.d(TAG, "playCorrectSound: "+i);
        switch (i){
            case 1:
                player.playYupi1Sound();
                break;
            case 2:
                player.playYupi2Sound();
                break;
            default:
                player.playYesSound();
        }
    }

    protected void setMenuScoreIcon(){
        Drawable drawable=null;
        if (game != null)
            drawable=game.getSmiley();
        if (game.getScore() == 0) {
            drawable = getResources().getDrawable(R.drawable.ic_sentiment_very_satisfied_white_24dp);
        }
        drawable.setTint(this.getResources().getColor(R.color.colorWhite));
        scoreItem.setIcon(drawable);
    }

    private ImageView setUpAnimationWin(int imageViewId){
        ImageView imageView = findViewById(imageViewId);
        imageView.setImageAlpha(230);
        imageView.setVisibility(View.INVISIBLE);
        return imageView;
    }

    protected void playWrongSong(){
        player.playOhOhSound();
    }

    protected void pauseAudio(){
        player.pauseAudio();
    }

    protected void stopAudio(){
        music.stop();
        player.stop();
    }

    protected void setVisibilityOptionsAndGuess(PictoView view1,PictoView view2,int visibility){
        view1.setVisibility(visibility);
        view2.setVisibility(visibility);
    }

    protected void showGuessItem(){
        switch (game.getGamelevel()){
            case 0:
                setVisibilityOptionsAndGuess(opcion3,guess3,View.INVISIBLE);
                setVisibilityOptionsAndGuess(opcion4,guess4,View.INVISIBLE);
                break;
            case 1:
                setVisibilityOptionsAndGuess(opcion3,guess3,View.VISIBLE);
                setVisibilityOptionsAndGuess(opcion4,guess4,View.INVISIBLE);
                break;
            case 2:
                setVisibilityOptionsAndGuess(opcion4,guess4,View.VISIBLE);
                break;
        }
    }

    public void hideText(PictoView option, JSONObject object){
        if(object!=null){
            option.setCustom_Img(json.getIcono(object));
            option.setInvisibleCustomTexto();
            option.setCustom_Texto(JSONutils.getNombre(object, ConfigurarIdioma.getLanguaje()));
        }
    }

    public void hidePictogramText(PictoView option){
        setGuessDrawable(option);
        setInvisibleText(option);
    }



}
