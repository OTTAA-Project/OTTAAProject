package com.stonefacesoft.ottaa.Games;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.InputDevice;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.stonefacesoft.ottaa.Custom_Picto;
import com.stonefacesoft.ottaa.Dialogos.DialogGameProgressInform;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GameControl;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFuntionGames;
import com.stonefacesoft.ottaa.utils.Audio.MediaPlayerAudio;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.utils.Ttsutils.UtilsTTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MatchPictograms extends AppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener, Make_Click_At_Time,View.OnTouchListener {
    private Custom_Picto opcion1;
    private Custom_Picto opcion2;
    private Custom_Picto opcion3;
    private Custom_Picto opcion4;
    private Custom_Picto lastPictogram;


    private Custom_Picto guess1;
    private Custom_Picto guess2;
    private Custom_Picto guess3;
    private Custom_Picto guess4;
    private Custom_Picto lastButton;
    private Custom_Picto animarPicto;
    private ScrollFuntionGames function_scroll;



    private SharedPreferences sharedPrefsDefault;
    private CustomToast dialogo;
    //Declaracion de variables del TTS
    private TextToSpeech mTTS;
    private UtilsTTS mUtilsTTS;
    private int PictoID;
    private int mPositionPadre;
    private int[] valoresCorrectos;

    private Json json;
    private JSONArray hijos;
    private JSONObject[] pictogramas;
    private JSONArray mjJsonArrayTodosLosGrupos;
    private ArrayList<Integer> numeros;


    private MediaPlayerAudio player;
    private MediaPlayerAudio music;

    private String name;

    private boolean isRepeatlection=false;
    private boolean repetirLeccion;
    private boolean isChecked;
    private boolean useHappySound;
    private boolean mute;


    private int lastPosicion;

    private Toolbar toolbar;
    private Menu mMenu;

    private Juego game;

    private AnimGameScore animGameScore;
    private ImageView mAnimationWin;

    private Handler handlerHablar;
    private AnalyticsFirebase analyticsFirebase;
    private BarridoPantalla barridoPantalla;
    private Button btnBarrido;
    private GameControl gameControl;


    private final Runnable animarHablar = new Runnable() {
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
        Intent intent = getIntent();
        boolean status_bar = intent.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.games_seleccionar_picto);
        dialogo=new CustomToast(this);
        PictoID = intent.getIntExtra("PictoID", 0);
        mPositionPadre = intent.getIntExtra("PositionPadre", 0);
        json = Json.getInstance();
        json.setmContext(this);
        toolbar=findViewById(R.id.toolbar);
        mjJsonArrayTodosLosGrupos=json.getmJSONArrayTodosLosGrupos();
        setSupportActionBar(toolbar);


        iniciarComponentes();
        handlerHablar=new Handler();
        hijos=json.getHijosGrupo2(mPositionPadre);
        numeros=new ArrayList<>();
        try {
            game=new Juego(this,1,json.getId(mjJsonArrayTodosLosGrupos.getJSONObject(mPositionPadre)));
            game.startUseTime();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        seleccionarPictogramas(0);
        seleccionarPictogramas(1);
        seleccionarPictogramas(2);
        seleccionarPictogramas(3);
        numeros.clear();
        cargarValores(0);
        cargarValores(1);
        cargarValores(2);
        cargarValores(3);


        guess1.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
        guess2.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
        guess3.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
        guess4.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));


        animGameScore = new AnimGameScore(this, mAnimationWin);


    }


    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.Option1:
                    lastPosicion=0;
                    lastPictogram=opcion1;
                    speakOption(opcion1);
                    hacerClickOpcion(true);
                    break;
                case R.id.Option2:
                    lastPosicion=1;
                    lastPictogram=opcion2;
                    speakOption(opcion2);
                    hacerClickOpcion(true);
                    break;
                case R.id.Option3:
                    lastPosicion=2;
                    lastPictogram=opcion3;
                    speakOption(opcion3);
                    hacerClickOpcion(true);
                    break;
                case R.id.Option4:
                    lastPosicion=3;
                    lastPictogram=opcion4;
                    speakOption(opcion4);
                    hacerClickOpcion(true);
                    break;
                case R.id.Guess1:
                    lastButton=guess1;
                    speakOption(guess1);
                    hacerClickOpcion(false);
                    break;
                case R.id.Guess2:
                    lastButton=guess2;
                    speakOption(guess2);
                    hacerClickOpcion(false);
                    break;
                case R.id.Guess3:
                    lastButton=guess3;
                    speakOption(guess3);
                    hacerClickOpcion(false);
                    break;
                case R.id.Guess4:
                    lastButton=guess4;
                    speakOption(guess4);
                    hacerClickOpcion(false);
                    break;
                case R.id.action_parar:
                    mute=!mute;
                    music.setMuted(mute);
                    sharedPrefsDefault.edit().putBoolean("muteSound",mute).apply();
//                    if(mute)
//                        sound_on_off.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_white_24dp));
//                    else
//                        sound_on_off.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
                    break;
                case R.id.btnBarrido:
                    onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
            }
    }


    private void habilitarPictoGrama(Custom_Picto picto,boolean esBoton) {
        picto.setEnabled(true);
        picto.setAlpha(1f);
        picto.setVisibility(View.VISIBLE);
        animarPictoReset(picto);
        if(esBoton)
            picto.setInvisibleCustomTexto();
    }


    public void habilitarDesHabilitarBotones(Button button){
        button.setEnabled(!button.isEnabled());
    }

    private void cargarPictogramas(){

    }

    private void iniciarComponentes(){
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
        dialogo=new CustomToast(this);
        pictogramas=new JSONObject[4];
        valoresCorrectos=new int[4];
        for (int i = 0; i <4 ; i++) {
            valoresCorrectos[i]=-1;
        }

        sharedPrefsDefault= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mute=sharedPrefsDefault.getBoolean("muteSound",false);
        isRepeatlection=sharedPrefsDefault.getBoolean("repetir",false);
//        if(mute)
//            sound_on_off.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_white_24dp));
//        else
//            sound_on_off.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
        isChecked=sharedPrefsDefault.getBoolean(getString(R.string.str_pistas),true);
        mUtilsTTS=new UtilsTTS(getApplicationContext(),mTTS,dialogo,sharedPrefsDefault);
        player=new MediaPlayerAudio(this);
        music=new MediaPlayerAudio(this);
        player.setVolumenAudio(0.15f);
        music.setVolumenAudio(0.05f);
        music.setMuted(mute);
        music.playMusic();

        mAnimationWin = findViewById(R.id.ganarImagen);
        mAnimationWin.setImageAlpha(230);
        mAnimationWin.setVisibility(View.INVISIBLE);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mUtilsTTS.hablarConDialogo(getString(R.string.joining_pictograms));
            }
        },800);
        iniciarBarrido();
        function_scroll=new ScrollFuntionGames(this);
        gameControl=new GameControl(this);
    }

    private void seleccionarPictogramas(int pos) {
        int value=(int)Math.round((Math.random()*hijos.length()-1)+0);

        if(!numeros.contains(value)) {
            numeros.add(value);
            try {
                JSONObject object=hijos.getJSONObject(value);
                pictogramas[pos] = hijos.getJSONObject(value);
                if(!json.getNombre(pictogramas[pos]).toLowerCase().equals("error"))
                cargarOpcion(pos);
                else
                    seleccionarPictogramas(pos);
            } catch (JSONException e) {
                e.printStackTrace();
                seleccionarPictogramas(pos);
            }
        }else{
            seleccionarPictogramas(pos);
        }
    }

    private void cargarOpcion(int pos){
        switch (pos){
            case 0:
                opcion1.setCustom_Img(json.getIcono(pictogramas[0]));
                opcion1.setInvisibleCustomTexto();
                opcion1.setCustom_Texto(json.getNombre(pictogramas[0]));
                break;
            case 1:
                opcion2.setCustom_Img(json.getIcono(pictogramas[1]));
                opcion2.setInvisibleCustomTexto();
                opcion2.setCustom_Texto(json.getNombre(pictogramas[1]));
                break;
            case 2:
                opcion3.setCustom_Img(json.getIcono(pictogramas[2]));
                opcion3.setInvisibleCustomTexto();
                opcion3.setCustom_Texto(json.getNombre(pictogramas[2]));

                break;
            case 3:
                opcion4.setCustom_Img(json.getIcono(pictogramas[3]));
                opcion4.setInvisibleCustomTexto();
                opcion4.setCustom_Texto(json.getNombre(pictogramas[3]));
                break;
        }

    }

    private void cargarValores(int pos) {
        int valor = (int) Math.round((Math.random() * 3 + 0));
        if (!numeros.contains(valor)) {
            numeros.add(valor);
            Log.d("Valor", "cargarValores: " + valor);
            cargarTextoBoton(valor, pos);

        } else {
            cargarValores(pos);
        }


    }

    private void decirPictoAleatorio(){
        if (animarPicto != null && animarPicto.getVisibility() == View.VISIBLE)
            animarPicto.setVisibility(View.INVISIBLE);
        if (numeros.size() < 4) {
            int valor = (int) Math.round(Math.random() * 3 + 0);
            if (!numeros.contains(valor)) {
                numeros.add(valor);
                name = json.getNombre(pictogramas[valor]);
                mUtilsTTS.hablar(name);
            } else {
                decirPictoAleatorio();
            }
        }
    }

    private void cargarTextoBoton(double valor,int pos){
        switch (pos){
            case 0:
                guess1.setCustom_Texto(json.getNombre(pictogramas[(int) valor]));
                break;
            case 1:
                guess2.setCustom_Texto(json.getNombre(pictogramas[(int) valor]));
                break;
            case 2:
                guess3.setCustom_Texto(json.getNombre(pictogramas[(int) valor]));
                break;
            case 3:
                guess4.setCustom_Texto(json.getNombre(pictogramas[(int) valor]));
                break;
        }
    }


    private void esCorrecto(boolean esPicto) {
        if(lastPictogram!=null&&lastButton!=null) {
            try {

                if (lastPictogram.getCustom_Texto().equals(lastButton.getCustom_Texto())) {
                    game.incrementCorrect();
                    selectRandomSoundWin();
                    lastPictogram.setVisibleText();
                    if (!useHappySound) {
                        useHappySound = true;
                    }
                    valoresCorrectos[lastPosicion] = 1;
                    bloquearOpcionPictograma(lastPosicion, lastButton);
                    lastButton=null;
                    lastPictogram=null;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //btn.setVisibility(View.INVISIBLE);
                                decirPictoAleatorio();
                            }
                        }, 2500);


                } else {
                    useHappySound = false;
                    player.playOhOhSound();
                    valoresCorrectos[lastPosicion] = 0;
                    game.incrementWrong();
                    animGameScore.animateCorrect(lastButton,game.getSmiley(Juego.DISSATISFIED));
                    if(esPicto)
                        lastPictogram=null;
                    else
                        lastButton=null;

                }
                if (verificarSiHayQueHacerReinicio()) {
                    if (isRepeatlection) {
                        repetirLeccion = !repetirLeccion;
                    }
                    cargarPuntos();
                    reiniciarLeccion();
                }
            } catch (Exception ex) {

            }
        }
    }

    private boolean verificarSiHayQueHacerReinicio(){
        for (int i = 0; i <valoresCorrectos.length ; i++) {
            if(valoresCorrectos[i]==-1||valoresCorrectos[i]==0)
                return false;
        }
        return true;
    }



    public void reiniciar(){
        player.pauseAudio();
        habilitarPictoGrama(guess1,false);
        habilitarPictoGrama(guess2,false);
        habilitarPictoGrama(guess3,false);
        habilitarPictoGrama(guess4,false);
        if(repetirLeccion){
            mUtilsTTS.hablarConDialogo(getString(R.string.repeat_pictograms));
            opcion1.setVisibility(View.INVISIBLE);
            opcion2.setVisibility(View.INVISIBLE);
            opcion3.setVisibility(View.INVISIBLE);
            opcion4.setVisibility(View.INVISIBLE);
        }

        if(!repetirLeccion){
            guess1.setVisibility(View.VISIBLE);
            guess2.setVisibility(View.VISIBLE);
            guess3.setVisibility(View.VISIBLE);
            guess4.setVisibility(View.VISIBLE);
            guess1.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess2.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess3.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess4.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            numeros.clear();
            habilitarPictoGrama(opcion1,true);
            habilitarPictoGrama(opcion2,true);
            habilitarPictoGrama(opcion3,true);
            habilitarPictoGrama(opcion4,true);
            seleccionarPictogramas(0);
            seleccionarPictogramas(1);
            seleccionarPictogramas(2);
            seleccionarPictogramas(3);
            numeros.clear();
            cargarValores(0);
            cargarValores(1);
            cargarValores(2);
            cargarValores(3);
        }else{
            numeros.clear();
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    decirPictoAleatorio();
                }
            },5000);

        }
        for (int i = 0; i <valoresCorrectos.length ; i++) {
            valoresCorrectos[i]=-1;
        }
    }

    //Aca cambia el
    private void bloquearOpcionPictograma(int opc, Custom_Picto btn){
        switch (opc){
            case 0:
                animarPictoGanador(opcion1, btn);

                break;
            case 1:
                animarPictoGanador(opcion2, btn);

                break;
            case 2:
                animarPictoGanador(opcion3, btn);

                break;
            case 3:
                animarPictoGanador(opcion4, btn);

                break;


        }
    }

    private void hacerClickOpcion(boolean esPicto){
        if(isChecked)
            handlerHablar.postDelayed(animarHablar,1000);
        if(lastButton!=null&&lastPictogram!=null) {
            if (!repetirLeccion)
                esCorrecto(esPicto);
        }else if(repetirLeccion){
            if ( lastButton.getCustom_Texto().equals(name)) {
                game.incrementCorrect();
                if (repetirLeccion) {

                    valoresCorrectos[GetPosicionLastButton()]=1;
                    if(!verificarSiHayQueHacerReinicio()){

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                decirPictoAleatorio();
                            }
                        }, 2500);
                    }else{
                        lastButton=null;
                        if (isRepeatlection) {
                            repetirLeccion = !repetirLeccion;
                        }
                        cargarPuntos();
                        reiniciar();
                    }
                }
                player.playYupi2Sound();
            } else {

                animGameScore.animateCorrect(lastButton,game.getSmiley(Juego.DISSATISFIED));
                game.incrementWrong();
                player.playOhOhSound();
            }
        }
    }

    private void speakOption(Custom_Picto option){
        //este es el onclick del pictograma

        player.pauseAudio();
        if(!repetirLeccion) {
            mUtilsTTS.hablar(option.getCustom_Texto());
        }
    }
    private void reiniciarLeccion(){
        Handler handler = new Handler();

        if (!repetirLeccion) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    reiniciar();
                }
            }, 2500);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reiniciar();
                }
            }, 2500);
        }
    }

    private void cargarPuntos(){
            game.getScoreClass().calcularValor();
            drawImageAtPosition();
    }

    //Anima el picto correctamente seleccionado
    private void animarPictoGanador(Custom_Picto from, Custom_Picto to) {

        TranslateAnimation animation = new TranslateAnimation(0, to.getX() - from.getX(), 0, to.getY() - from.getY());
        animation.setRepeatMode(Animation.ABSOLUTE);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                from.setAlpha(0);
                from.setVisibility(View.INVISIBLE);
                animation.cancel();
                animGameScore.animateCorrect(to,game.getSmiley(Juego.VERY_SSATISFIED));
                to.setCustom_Img(from.getCustom_Imagen());
                from.setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        from.startAnimation(animation);
    }

    private void animarPictoReset(Custom_Picto picto) {

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0);
        animation.setRepeatMode(0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        picto.startAnimation(animation);

    }


    private void selectRandomSoundWin() {
         player.playYesSound();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //la variable temporal galeria grupos se la usa para modificar puntaje



        player.stop();
            music.stop();
        Intent databack = new Intent();
        databack.putExtra("Boton", mPositionPadre);
        setResult(3, databack);
        game.endUseTime();
        game.guardarObjetoJson();
        game.subirDatosJuegosFirebase();
        this.finish();

    }

    private int GetPosicionLastButton(){
        switch (lastButton.getId()){
            case R.id.Guess1:
                guess1.setAlpha(0);
                return 0;
            case R.id.Guess2:
                guess2.setAlpha(0);
                return 1;
            case R.id.Guess3:
                guess3.setAlpha(0);
                return 2;
            case R.id.Guess4:
                guess4.setAlpha(0);
                return 3;
        }
        return 0;
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_game, menu);
        mMenu=menu;
        Drawable drawable=game.devolverCarita();
        if(game.getScore()==0)
            drawable=getResources().getDrawable(R.drawable.ic_sentiment_very_satisfied_white_24dp);
        drawable.setTint(getResources().getColor(R.color.colorWhite));
        mMenu.getItem(0).setIcon(drawable);
        mMenu.getItem(0).setVisible(true);
        menu.getItem(2).setOnMenuItemClickListener(this);
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setOnMenuItemClickListener(this);
        menu.getItem(1).setVisible(true);
        menu.getItem(0).setOnMenuItemClickListener(this);
        menu.getItem(3).setOnMenuItemClickListener(this);

        setIcon(mMenu.getItem(3),mute,R.drawable.ic_volume_off_white_24dp,R.drawable.ic_volume_up_white_24dp);
        setIcon(mMenu.getItem(1),isChecked,R.drawable.ic_live_help_white_24dp,R.drawable.ic_unhelp);
        setIcon(mMenu.getItem(2),isRepeatlection,R.drawable.ic_repeat_white_24dp,R.drawable.ic_unrepeat_ic_2);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_parar:
                analyticsFirebase.customEvents("Touch","Match Pictograms","Mute");
                mute=!mute;
                sharedPrefsDefault.edit().putBoolean("muteSound",mute).apply();
                music.setMuted(mute);
                setIcon(item,mute,R.drawable.ic_volume_off_white_24dp,R.drawable.ic_volume_up_white_24dp);
                return true;
            case R.id.check:
                analyticsFirebase.customEvents("Touch","Match Pictograms","Help Action");
                isChecked=!isChecked;
                sharedPrefsDefault.edit().putBoolean(getString(R.string.str_pistas), isChecked).apply();
                setIcon(mMenu.getItem(1),isChecked,R.drawable.ic_live_help_white_24dp,R.drawable.ic_unhelp);
                if (isChecked) {
                    handlerHablar.postDelayed(animarHablar, 4000);
                    dialogo.mostrarFrase(getString(R.string.help_function));
                } else {
                    handlerHablar.removeCallbacks(animarHablar);
                    dialogo.mostrarFrase(getString(R.string.help_function_disabled));
                }
                return true;
            case R.id.score:
                analyticsFirebase.customEvents("Touch","Match Pictograms","Score Dialog");
                DialogGameProgressInform inform=new DialogGameProgressInform(this,R.layout.game_progress_score,game);
                inform.cargarDatosJuego();
                inform.showDialog();
                return true;
            case R.id.repeat:
                analyticsFirebase.customEvents("Touch","Match Pictograms","Repeat");
                isRepeatlection=!isRepeatlection;
                sharedPrefsDefault.edit().putBoolean("repetir",isRepeatlection).apply();

                setIcon(item,isRepeatlection,R.drawable.ic_repeat_white_24dp,R.drawable.ic_unrepeat_ic_2);
                if(isRepeatlection){
                    dialogo.mostrarFrase(getString(R.string.repeat_function_activated));
                }
                else {
                    dialogo.mostrarFrase(getString(R.string.repeat_function_disabled));
                }
                return true;
        }
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

    private void drawImageAtPosition(){
        Drawable drawable=game.devolverCarita();
        drawable.setTint(getResources().getColor(R.color.colorWhite));
        mMenu.getItem(0).setIcon(drawable);
    }


    private void animateGanador(Custom_Picto picto_ganador,int tipo){
        switch (tipo){
            case 0:
                selectButtonGanador(picto_ganador.getCustom_Texto()).startAnimation(AnimationUtils.loadAnimation(MatchPictograms.this, R.anim.shake));
                break;
            case 1:
                selectImagenGanadora(picto_ganador.getCustom_Texto()).startAnimation(AnimationUtils.loadAnimation(MatchPictograms.this, R.anim.shake));
                break;
        }
    }

    private Custom_Picto selectButtonGanador(String text){
        if(guess1.getCustom_Texto().equals(text))
            return guess1;
        else if(guess2.getCustom_Texto().equals(text))
            return guess2;
        else if(guess3.getCustom_Texto().equals(text))
            return guess3;
        else
            return guess4;

    }

    private Custom_Picto selectImagenGanadora(String text){
        if(opcion1.getCustom_Texto().equals(text))
            return opcion1;
        else if(opcion2.getCustom_Texto().equals(text))
            return opcion2;
        else if(opcion3.getCustom_Texto().equals(text))
            return opcion3;
        else
            return opcion4;

    }

    private void setIcon(MenuItem item,boolean status,int dEnabled,int dDisabled){

        if (status) {
            item.setIcon(getResources().getDrawable(dEnabled));
        } else {
            item.setIcon(getResources().getDrawable(dDisabled));
        }
    }

    private void iniciarBarrido() {
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
        barridoPantalla = new BarridoPantalla(this, listadoObjetosBarrido, this);
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
}
