package com.stonefacesoft.ottaa.Games;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.perf.metrics.AddTrace;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.DialogGameProgressInform;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictogramsFourOptions;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Games.GamesSettings;
import com.stonefacesoft.ottaa.Games.Model.WhichIsThePictoModel;
import com.stonefacesoft.ottaa.Interfaces.Lock_Unlocked_Pictograms;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GameControl;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFuntionGames;
import com.stonefacesoft.ottaa.utils.Audio.MediaPlayerAudio;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.Ttsutils.UtilsGamesTTS;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


//Merge realizado
public class WhichIsThePicto extends GameViewSelectPictogramsFourOptions {

    private static final String TAG = "WhichIsThePicto";
    //Handler para animar la respuesat correcta luego de un tiempo si no se presiona
    private final Handler handlerHablar = new Handler();
    private final Handler decirPicto = new Handler();
    //
    //Declaracion de variables del TTS
    private TextToSpeech mTTS;
    //Question Button
    //Winner imageview
    // Answer button



    //Pistas
    private boolean primerUso;
    private int ganadorAnterior = -1;
    private WhichIsThePictoModel model;
    //RatingStar
    private RatingBar Puntaje;
    //Declaramos el media player
    private PictoView viewGanador;
    private final Runnable animarHablar = new Runnable() {
        @Override
        public void run() {
            viewGanador.startAnimation(AnimationUtils.loadAnimation(WhichIsThePicto.this, R.anim.shake));
            if (sharedPrefsDefault.getBoolean(getString(R.string.str_pistas), false))
                handlerHablar.postDelayed(this, 4000);
        }
    };
    private final Runnable talkGanador = new Runnable() {
        @Override
        public void run() {
            imageButton.callOnClick();
        }
    };
    //Pictos en juego
    private int[] pictos;

    //Jsons
    private Json json;
    private JSONArray mJsonArrayTodosLosPictos;
    private JSONArray mJsonArrayTodosLosGrupos;
    // Datos que le paso por el intent!!!
    //Handler para animar el Hablar cuando pasa cierto tiempo
    private int cantVecInc;
    private Toolbar toolbar;
    private AnimGameScore animGameScore;
    private Menu mMenu;
    private MenuItem scoreItem;
    private Juego game;
    //Handler para animar el Hablar cuando pasa cierto tiempo
    private final Runnable animarGano = new Runnable() {
        @Override
        public void run() {
            PrimerNivel();
        }
    };
    private ImageView imageView;

    private BarridoPantalla barridoPantalla;
    private ScrollFuntionGames function_scroll;

    //  private FirebaseAnalytics firebaseAnalytics;
    private GameControl gameControl;
    private GamesSettings gamesSettings;
    private boolean mTutorialFlag = true;

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
        music.stop();
        mUtilsTTS.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @AddTrace(name = "WhichIsThePicto", enabled = true /* optional */)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Ocultamos la barra de notificaciones
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        initComponents();
        model = new WhichIsThePictoModel();
        dialogo = CustomToast.getInstance(this);


        setToolbarName(toolbar,R.string.whichpictogram);
        context = this;
        primerUso = true;
        //Implemento el manejador de preferencias
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        initUtilsTTS(sharedPrefsDefault);
        gamesSettings = new GamesSettings();
        gamesSettings.enableSound(sharedPrefsDefault.getBoolean("muteSound", false)).enableHelpFunction(sharedPrefsDefault.getBoolean(getString(R.string.str_pistas), true));
        music.setMuted(gamesSettings.isSoundOn());
        music.playMusic();
        //Declaramos el boton para que reproduzca el tts con lo que tiene que decir


        //Implementacion de los botones de la pregunta


        Json.getInstance().setmContext(this);
        json = Json.getInstance();

        try {
            mJsonArrayTodosLosGrupos = json.readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS);
            int id = json.getId(mJsonArrayTodosLosGrupos.getJSONObject(mPositionPadre));
            analitycsFirebase.levelNameGame(TAG + "_" + JSONutils.getNombre(mJsonArrayTodosLosGrupos.getJSONObject(mPositionPadre),sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));

            game = new Juego(this, 0, id);
            game.setGamelevel(sharedPrefsDefault.getInt("whichIsThePictoLevel",0));
            game.setMaxStreak(20);
            game.setMaxLevel(2);
            game.startUseTime();
            loadLevel();
        } catch (JSONException | FiveMbException e) {
            e.printStackTrace();
        }

        try {
            mJsonArrayTodosLosPictos = JSONutils.getHijosGrupo2(json.getmJSONArrayTodosLosPictos(),mJsonArrayTodosLosGrupos.getJSONObject(mPositionPadre));
        } catch (Exception e) {
            Toast.makeText(context, getApplicationContext().getResources().getString(R.string.no_hay_pictos), Toast.LENGTH_SHORT).show();
        }

        //Puntaje

        if(mJsonArrayTodosLosPictos.length()>4){
            mTutorialFlag = sharedPrefsDefault.getBoolean("PrimerUsoJuegos", true);
            animGameScore = new AnimGameScore(this, mAnimationWin);
            function_scroll = new ScrollFuntionGames(this);
            iniciarBarrido();
            gameControl = new GameControl(this);
            PrimerNivel();
            hideView();
        }else{
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //la variable temporal galeria grupos se la usa para modificar puntaje
        json.setmJSONArrayTodosLosGrupos(mJsonArrayTodosLosGrupos);
        if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
            Log.e(TAG, "onBackPressed: Error al guardar grupos ");

        game.endUseTime();
        game.saveJsonObjects();
        game.uploadFirebaseGameData();

        handlerHablar.removeCallbacks(animarHablar);
        decirPicto.removeCallbacks(talkGanador);
        mediaPlayer.stop();
        music.stop();
        Intent databack = new Intent();
        databack.putExtra("Boton", mPositionPadre);
        sharedPrefsDefault.edit().putInt("whichIsThePictoLevel",game.getGamelevel()).apply();
        setResult(3, databack);
        this.finish();
    }

    private void Alert() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(getResources().getString(R.string.pref_important_alert));
        dialogo1.setCancelable(false);
        dialogo1.setMessage(getResources().getString(R.string.pref_error_112));
        //dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(getResources().getString(R.string.pref_yes_alert), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });
        AlertDialog dialog = dialogo1.create();
        dialog.show();
    }

    private void PrimerNivel() {
        if (sharedPrefsDefault.getBoolean(getString(R.string.str_pistas), false))
            handlerHablar.postDelayed(animarHablar, 10000);
        cargarPictos();

    }

    private void liberaMemoria() {
        this.onLowMemory();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.onTrimMemory(TRIM_MEMORY_MODERATE);
        Log.e("NotiGame", "Trimming memory");
    }

    private int elegirGanador(int... pictos) {
        int ganador = devolverOpcionGanadora(model.elegirGanador());
        switch (ganador) {
            case 0:
                viewGanador = Opcion1;
                break;
            case 1:
                viewGanador = Opcion2;
                break;
            case 2:
                viewGanador = Opcion3;
                break;
            case 3:
                viewGanador = Opcion4;
                break;
        }
        return pictos[ganador];
    }

    private boolean esGanador(PictoView valor, PictoView ganadorLvl) {
        if (valor.getCustom_Texto().equals(ganadorLvl.getCustom_Texto())) {
            bloquearPictos();
            return true;
        }
        return false;
    }

    private boolean resolveIntent() {
        PackageManager pm = getPackageManager();
        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        ResolveInfo resolveInfo = pm.resolveActivity(installIntent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfo != null;
    }

    //Carga el color correspondienta a cada picto


    //private void cargarPictos(int picto1,int picto2,int picto3,int picto4){
    private void cargarPictos() {
        //animarReset();
        if (mJsonArrayTodosLosPictos != null) {
            Seleccion1.setCustom_Img(getResources().getDrawable(R.drawable.ic_help_outline_black_24dp));
            loadValue();
            try {
                cargarDatosOpcion(model.getValue(0), Opcion1, 0);
                cargarDatosOpcion(model.getValue(1), Opcion2, 1);
                if (game.getGamelevel() >= 1)
                    cargarDatosOpcion(model.getValue(2), Opcion3, 2);
                if (game.getGamelevel() >= 2)
                    cargarDatosOpcion(model.getValue(3), Opcion4, 3);
            } catch (Exception e) {
            }
        }
        handlerHablar.postDelayed(talkGanador, 600);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Option1:
                actionGanador(Opcion1);
                break;
            case R.id.Option2:
                actionGanador(Opcion2);
                break;
            case R.id.Option3:
                actionGanador(Opcion3);
                break;
            case R.id.Option4:
                actionGanador(Opcion4);
                break;
            case R.id.ttsJuego:
                if (viewGanador != null && !this.isFinishing()) {
                    mUtilsTTS.hablar(getApplicationContext().getResources().getString(R.string.pref_cual_es) + ", " +
                            viewGanador.getCustom_Texto() + "?");
                    if (primerUso) {
                        viewGanador.startAnimation(AnimationUtils.loadAnimation(WhichIsThePicto.this, R.anim.shake));
                        primerUso = false;
                    }
                }
                break;
            case R.id.btnBarrido:
                onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
                break;
        }
    }

    @Override
    protected void onPause() {

        Json.getInstance().setmContext(this);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (music != null) {
            music.stop();
            music.playMusic();
        }
        Json.getInstance().setmContext(this);
    }


    private float CalcularPuntaje(boolean Acerto) {
        if (Acerto) {
            game.incrementCorrect();
            game.incrementTimesRight();
            Drawable drawable = game.getSmiley();
            drawable.setTint(getResources().getColor(R.color.colorWhite));
            mMenu.getItem(0).setIcon(drawable).setVisible(true);
            if (game.isChangeLevel()) {
                game.changeLevelGame();
                loadLevel();
            }

        } else {
            game.incrementWrong();

            Drawable drawable = game.getSmiley();
            drawable.setTint(getResources().getColor(R.color.colorWhite));
            mMenu.getItem(0).setIcon(drawable).setVisible(true);

        }
        return (float) getPuntaje();
    }

    private double getPuntaje() {
        return game.getScoreClass().getResult();
    }


    private void actionGanador(PictoView picto) {
        if (esGanador(picto, viewGanador)) {
            if (cantVecInc == 0)
                mediaPlayer.playYesSound();
            else
                mediaPlayer.playYupi2Sound();
            Seleccion1.setUpContext(this);
            Seleccion1.setUpGlideAttatcher(this);
            Seleccion1.setPictogramsLibraryPictogram(new Pictogram(picto.getPictogram().toJsonObject(),ConfigurarIdioma.getLanguaje()));
            picto.setCustom_Img(getResources().getDrawable(R.drawable.ic_bien));
            picto.setCustom_Color(getResources().getColor(R.color.LightGreen));
            handlerHablar.removeCallbacks(animarHablar);
            //animarGanador(picto);

            decirPicto.postDelayed(animarGano, 3000);
            //Seteo el puntaje
            model.refreshValueIndex();
            CalcularPuntaje(true);
            setMenuScoreIcon();
            cantVecInc = 0;
            animGameScore.animateCorrect(picto, game.getSmiley(Juego.SATISFIED));

        } else {
            mediaPlayer.playOhOhSound();
            picto.setCustom_Img(getResources().getDrawable(R.drawable.ic_mal));
            picto.setCustom_Color(getResources().getColor(R.color.Red));
            cantVecInc++;
            //Seteo el puntaje
            CalcularPuntaje(false);
            setMenuScoreIcon();
            animGameScore.animateCorrect(picto, game.getSmiley(Juego.DISSATISFIED));

        }
    }

    private int devolverOpcionGanadora(int value) {
        if (ganadorAnterior == -1) {
            ganadorAnterior = value;
            return value;
        }
        Log.e(TAG, "devolverOpcionGanadora: " + value);
        if (ganadorAnterior == value) {
            devolverValor((int) (Math.random() * 4),0);
        }
        ganadorAnterior = value;
        return value;
    }

    private int devolverValor(int value,int cont) {
        int sum = cont;
        if(sum>16)
            return -1;
        if (!model.hasTheValue(value))
            return value;
        else
            devolverValor(Math.round((float) Math.random() * mJsonArrayTodosLosPictos.length()),sum++);
        return -1;
    }

    //Metodos tts we can implements them because the tts start later
    //Anima el picto correctamente seleccionado


    private void cargarDatosValors(int position) {
        int valor = (int) (Math.random() * mJsonArrayTodosLosPictos.length());
        boolean tieneValor = false;
        if (model.validatePosition(position)) {
            for (int i = 0; i < model.getValueIndex().length; i++) {
                if (model.isTheValue(i, valor))
                    tieneValor = true;
            }
            if (!tieneValor)
                model.loadValue(position, valor);
        }

    }


    private void cargarDatosOpcion(int position, PictoView option, int pos) {
        try {
            if (mJsonArrayTodosLosPictos.getJSONObject(position) != null) {
                if (!JSONutils.getNombre(mJsonArrayTodosLosPictos.getJSONObject(position),sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")).equals("")) {
                    option.setUpContext(this);
                    option.setUpGlideAttatcher(this);
                    option.setPictogramsLibraryPictogram(new Pictogram(mJsonArrayTodosLosPictos.getJSONObject(position), ConfigurarIdioma.getLanguaje()));
                    model.loadValue(pos, position);
                } else {
                    position = devolverValor(Math.round((float) Math.random() * mJsonArrayTodosLosPictos.length()),0);
                    cargarDatosValors(pos);
                    cargarDatosOpcion(position, option, pos);
                }
            } else {
                position = devolverValor(Math.round((float) Math.random() * mJsonArrayTodosLosPictos.length()),0);
                model.loadValue(pos, position);
                cargarDatosOpcion(position, option, pos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            position = devolverValor(Math.round((float) Math.random() * mJsonArrayTodosLosPictos.length()),0);
            model.loadValue(pos, position);
            cargarDatosOpcion(position, option, pos);
        } finally {
            if (pos == model.getValueIndex().length-1) {
                elegirGanador();
                Seleccion1.setCustom_Img(getResources().getDrawable(R.drawable.agregar_picto_transp));
                Log.d(TAG, "cargarDatosOpcion: " + viewGanador.getCustom_Texto());
            }
        }
    }

    private void desbloquearPictos() {
        Opcion1.setEnabled(true);
        Opcion2.setEnabled(true);
        Opcion3.setEnabled(true);
        Opcion4.setEnabled(true);

    }

    private void bloquearPictos() {
        Opcion1.setEnabled(false);
        Opcion2.setEnabled(false);
        Opcion3.setEnabled(false);
        Opcion4.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        mMenu = menu;
        inflater.inflate(R.menu.action_bar_game, mMenu);
        // mMenu = menu;
        scoreItem = menu.findItem(R.id.score);
        // animGameScore.setMenuView(mMenu);
        setMenuScoreIcon();
        mMenu.getItem(0).setVisible(true);
        menu.getItem(2).setVisible(false);
        setMenuScoreIcon();
        mMenu.getItem(0).setOnMenuItemClickListener(this::onMenuItemClick);
        mMenu.getItem(1).setOnMenuItemClickListener(this::onMenuItemClick);
        mMenu.getItem(3).setOnMenuItemClickListener(this::onMenuItemClick);

        setIcon(mMenu.getItem(3), gamesSettings.isSoundOn(), R.drawable.ic_volume_off_white_24dp, R.drawable.ic_volume_up_white_24dp);
        setIcon(mMenu.getItem(1), gamesSettings.isHelpFunction(), R.drawable.ic_live_help_white_24dp, R.drawable.ic_unhelp);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_parar:
                gamesSettings.enableSound(gamesSettings.changeStatus(gamesSettings.isSoundOn()));
                sharedPrefsDefault.edit().putBoolean("muteSound", gamesSettings.isSoundOn()).apply();
                music.setMuted(gamesSettings.isSoundOn());
                setIcon(item, gamesSettings.isSoundOn(), R.drawable.ic_volume_off_white_24dp, R.drawable.ic_volume_up_white_24dp);
                analitycsFirebase.customEvents("Touch", "WhichIsThePicto", "Mute");
                return true;
            case R.id.score:
                DialogGameProgressInform inform = new DialogGameProgressInform(this, R.layout.game_progress_score, game);
                inform.cargarDatosJuego();
                inform.showDialog();
                analitycsFirebase.customEvents("Touch", "WhichIsThePicto", "Score Dialog");
                return true;
            case R.id.check:
                gamesSettings.enableHelpFunction(gamesSettings.changeStatus(gamesSettings.isHelpFunction()));
                analitycsFirebase.customEvents("Touch", "WhichIsThePicto", "Help Action");
                sharedPrefsDefault.edit().putBoolean(getString(R.string.str_pistas), gamesSettings.isHelpFunction()).apply();
                setIcon(item, gamesSettings.isHelpFunction(), R.drawable.ic_live_help_white_24dp, R.drawable.ic_unhelp);
                if (gamesSettings.isHelpFunction()) {
                    handlerHablar.postDelayed(animarHablar, 4000);
                    dialogo.mostrarFrase(getString(R.string.help_function));
                } else {
                    handlerHablar.removeCallbacks(animarHablar);
                    dialogo.mostrarFrase(getString(R.string.help_function_disabled));
                }
                return true;
        }
        return false;
    }

    private void setIcon(MenuItem item, boolean status, int dEnabled, int dDisabled) {
        if (status) {
            item.setIcon(getResources().getDrawable(dEnabled));
        } else {
            item.setIcon(getResources().getDrawable(dDisabled));
        }
    }

    @Override
    public void lockPictogram(boolean isSpeaking) {
            bloquearPictos();
    }

    @Override
    public void unlockPictogram() {
        desbloquearPictos();
    }

    private void iniciarBarrido() {
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        listadoObjetosBarrido.add(imageButton);
        listadoObjetosBarrido.add(Opcion1);
        listadoObjetosBarrido.add(Opcion2);
        listadoObjetosBarrido.add(Opcion3);
        listadoObjetosBarrido.add(Opcion4);

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
        } else {
            btnBarrido.setVisibility(View.GONE);
        }


    }

    @Override
    public void OnClickBarrido() {
        if (function_scroll.isClickEnabled() && barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).getId() == R.id.btnTodosLosPictos)
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        else if (!function_scroll.isClickEnabled()) {
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gameControl.makeClick(event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_SCROLL:

                    if (barridoPantalla.isScrollMode() || barridoPantalla.isScrollModeClicker()) {
                        if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) {
                            if (barridoPantalla.isScrollMode())
                                function_scroll.HacerClickEnTiempo();
                            barridoPantalla.avanzarBarrido();
                        } else {
                            if (barridoPantalla.isScrollMode())
                                function_scroll.HacerClickEnTiempo();
                            barridoPantalla.volverAtrasBarrido();

                        }
                    }
                    return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameControl.makeClick(event);
    }

    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }

    private void setMenuScoreIcon() {
        Drawable drawable = null;
        if (game != null)
            drawable = game.getSmiley();
        if (game.getScore() == 0) {
            drawable = getResources().getDrawable(R.drawable.ic_sentiment_very_satisfied_white_24dp);
        }
        drawable.setTint(this.getResources().getColor(R.color.colorWhite));
        scoreItem.setIcon(drawable);
    }

    public void hideView() {
        if (game.getGamelevel() == 0) {
            Opcion3.setVisibility(View.INVISIBLE);
            Opcion4.setVisibility(View.INVISIBLE);
        } else if (game.getGamelevel() == 1) {
            Opcion4.setVisibility(View.INVISIBLE);
        }
    }

    public void loadLevel() {
        switch (game.getGamelevel()) {
            case 0:
                model.setSize(2);
                model.createArray();
                model.refreshValueIndex();
                break;
            case 1:
                model.setSize(3);
                model.createArray();
                model.refreshValueIndex();
                Opcion3.setVisibility(View.VISIBLE);
                break;
            case 2:
                model.setSize(4);
                model.createArray();
                model.refreshValueIndex();
                Opcion4.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void loadValue() {
        cargarDatosValors(0);
        cargarDatosValors(1);
        if (game.getGamelevel() >= 1)
            cargarDatosValors(2);
        if (game.getGamelevel() >= 2)
            cargarDatosValors(3);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (barridoPantalla.isBarridoActivado()) {

            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                event.startTracking();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                event.startTracking();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                event.startTracking();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                event.startTracking();
                return true;
            }
            if(keyCode == KeyEvent.KEYCODE_BACK){
                if(event.getSource() == InputDevice.SOURCE_MOUSE)
                    barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).callOnClick();
                else
                    onBackPressed();
                return true;
            }

        }else{
            if(keyCode == KeyEvent.KEYCODE_BACK){
                 onBackPressed();
                return true;
            }
        }

        return false;
    }


}