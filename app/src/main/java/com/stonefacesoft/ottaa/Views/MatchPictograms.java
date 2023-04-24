package com.stonefacesoft.ottaa.Views;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.firebase.perf.metrics.AddTrace;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.DialogGameProgressInform;
import com.stonefacesoft.ottaa.Games.Model.MatchPictogramsModel;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONException;

public class MatchPictograms extends GameViewSelectPictograms {

    private final Runnable animarHablar = new Runnable() {
        @Override
        public void run() {
            if (lastButton != null)
                animateGanador(lastButton, 1);
            else if (lastPictogram != null)
                animateGanador(lastPictogram, 0);
            if (sharedPrefsDefault.getBoolean(getString(R.string.str_pistas), false))
                handlerHablar.postDelayed(this, 4000);
        }
    };
    private MatchPictogramsModel model;

    @AddTrace(name = "MatchPictograms", enabled = true /* optional */)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(hijos.length()>4){
            showDescription(getString(R.string.join_pictograms));
      //  mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            model = new MatchPictogramsModel();
            try {
                setUpGame(1,json.getId(mjJsonArrayTodosLosGrupos.getJSONObject(mPositionPadre)));
                game.setGamelevel(sharedPrefsDefault.getInt("MatchPictogramsLevel",0));
                game.setMaxLevel(3);
                game.setMaxStreak(16);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            loadModel();
            model.refreshValueIndex();
            selectRandomOptions();
            numeros.clear();
            loadLevePictograms();
            numeros.clear();
            guess1.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess2.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess3.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess4.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            animGameScore = new AnimGameScore(this, mAnimationWin);
        }else{
            onBackPressed();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Option1:
                selectOption(0,opcion1);
                break;
            case R.id.Option2:
                selectOption(1,opcion2);
                break;
            case R.id.Option3:
                selectOption(2,opcion3);
                break;
            case R.id.Option4:
                selectOption(3,opcion4);
                break;
            case R.id.Guess1:
                selectGuessOption(guess1);
                break;
            case R.id.Guess2:
                selectGuessOption(guess2);
                break;
            case R.id.Guess3:
                selectGuessOption(guess3);
                break;
            case R.id.Guess4:
                selectGuessOption(guess4);
                break;
            case R.id.action_parar:
                gamesSettings.enableSound(gamesSettings.changeStatus(gamesSettings.isSoundOn()));
                music.setMuted(gamesSettings.isSoundOn());
                sharedPrefsDefault.edit().putBoolean("muteSound", gamesSettings.isSoundOn()).apply();
                break;
            case R.id.btnBarrido:
                onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        }
    }


    public void habilitarDesHabilitarBotones(Button button) {
        button.setEnabled(!button.isEnabled());
    }



    protected void selectRandomPictogram(int pos) {
        int value = (int) Math.round((Math.random() * hijos.length() - 1) + 0);
        if (!numeros.contains(value)) {
            numeros.add(value);
            try {
                pictogramas[pos] = hijos.getJSONObject(value);
                if(!JSONutils.getNombre(pictogramas[pos], ConfigurarIdioma.getLanguaje()).equalsIgnoreCase("error"))
                cargarOpcion(pos);
                else
                    selectRandomPictogram(pos);
            } catch (JSONException e) {
                e.printStackTrace();
                selectRandomPictogram(pos);
            }
        } else {
            selectRandomPictogram(pos);
        }
    }


    protected void cargarOpcion(int pos) {
        switch (pos) {
            case 0:
                hideText(opcion1,pictogramas[0]);
                break;
            case 1:
                hideText(opcion2,pictogramas[1]);
                break;
            case 2:
                hideText(opcion3,pictogramas[2]);
                break;
            case 3:
                hideText(opcion4,pictogramas[3]);
                break;
        }

    }

    protected void cargarValores(int pos) {


    }


    protected void cargarTextoBoton(double valor, int pos) {
        switch (pos) {
            case 0:
                guess1.setCustom_Texto(JSONutils.getNombre(pictogramas[(int) valor],ConfigurarIdioma.getLanguaje()));
                break;
            case 1:
                guess2.setCustom_Texto(JSONutils.getNombre(pictogramas[(int) valor],ConfigurarIdioma.getLanguaje()));
                break;
            case 2:
                guess3.setCustom_Texto(JSONutils.getNombre(pictogramas[(int) valor],ConfigurarIdioma.getLanguaje()));
                break;
            case 3:
                guess4.setCustom_Texto(JSONutils.getNombre(pictogramas[(int) valor],ConfigurarIdioma.getLanguaje()));
                break;
        }
    }


    protected void esCorrecto(boolean esPicto) {
        if (gamesSettings.isRepeatLection()) {
            if(lastButton!= null){
                checkName(lastButton.getCustom_Texto(),name,esPicto);
            }
        } else {
            if (lastPictogram != null && lastButton != null) {
                try {
                    checkName(lastPictogram.getCustom_Texto(),lastButton.getCustom_Texto(),esPicto);
                } catch (Exception ex) {
                    Log.e(TAG, "Exception message at esCorrecto: " + ex.getMessage());
                }
            }
        }
    }


    public void checkName(String option1, String option2, boolean isPictogram){
        if(option1.equals(option2)){
            CorrectAction();
        }else{
            WrongAction(isPictogram);
        }
    }

    protected boolean verificarSiHayQueHacerReinicio() {
        return model.restartValue();
    }


    public void reset() {
        pauseAudio();
        if (game.isChangeLevel()&&!gamesSettings.isRepeatLection()){
            game.changeLevelGame();
            loadModel();
        }
        for (int i = 0; i < model.getSize(); i++) {
            enableGuessOption(i);
        }
        if (gamesSettings.isRepeatLection()) {
            mTTS.getUtilsTTS().hablarConDialogo(getString(R.string.repeat_pictograms));
            opcion1.setVisibility(View.INVISIBLE);
            opcion2.setVisibility(View.INVISIBLE);
            opcion3.setVisibility(View.INVISIBLE);
            opcion4.setVisibility(View.INVISIBLE);
            numeros.clear();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    decirPictoAleatorio();
                }
            }, 5000);
        }
        if (!gamesSettings.isRepeatLection()) {
            guess1.setVisibility(View.VISIBLE);
            guess2.setVisibility(View.VISIBLE);
            guess3.setVisibility(View.VISIBLE);
            guess4.setVisibility(View.VISIBLE);
            guess1.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess2.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess3.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess4.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            numeros.clear();
            for (int i = 0; i < model.getSize(); i++) {
                enablePictogram(i);
            }
            for (int i = 0; i < model.getSize(); i++) {
                selectRandomPictogram(i);
            }
            numeros.clear();
            loadLevePictograms();
        }
        model.refreshValueIndex();
    }


    protected void makeClickOption(boolean esPicto) {
        if (gamesSettings.isHelpFunction())
            handlerHablar.postDelayed(animarHablar, 1000);
        esCorrecto(esPicto);
    }

    protected void speakOption(PictoView option) {
        super.speakOption(option);
        if (!gamesSettings.isRepeatLection()) {
            mTTS.getUtilsTTS().hablar(option.getCustom_Texto());
        }
    }

    protected void restartLection() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 2500);

    }

    protected void cargarPuntos() {
        game.getScoreClass().getResult();
        drawImageAtPosition();
    }

    protected void animateWinnerPictogram(PictoView from, PictoView to) {
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
                animGameScore.animateCorrect(to, game.getSmiley(Juego.VERY_SSATISFIED));
                to.setCustom_Img(from.getCustom_Imagen());
                from.setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        from.startAnimation(animation);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(game!=null){
            sharedPrefsDefault.edit().putInt("MatchPictogramsLevel",game.getGamelevel()).apply();
            game.endUseTime();
            game.saveJsonObjects();
            game.uploadFirebaseGameData();
        }
        stopAudio();
        Intent databack = new Intent();
        setResult(3, databack);
        databack.putExtra("Boton", mPositionPadre);
        this.finish();

    }

    protected int GetPosicionLastButton() {
        switch (lastButton.getId()) {
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
    protected void onResume() {
        super.onResume();
        if (music != null) {
            music.stop();
            music.playMusic();
        }
        Json.getInstance().setmContext(this);
    }

    protected void drawImageAtPosition() {
        Drawable drawable = game.getSmiley();
        drawable.setTint(getResources().getColor(R.color.colorWhite));
        mMenu.getItem(0).setIcon(drawable);
    }



    protected PictoView selectButtonGanador(String text) {
        if (guess1.getCustom_Texto().equals(text))
            return guess1;
        else if (guess2.getCustom_Texto().equals(text))
            return guess2;
        else if (guess3.getCustom_Texto().equals(text))
            return guess3;
        else
            return guess4;
    }

    protected PictoView selectImagenGanadora(String text) {
        if (opcion1.getCustom_Texto().equals(text))
            return opcion1;
        else if (opcion2.getCustom_Texto().equals(text))
            return opcion2;
        else if (opcion3.getCustom_Texto().equals(text))
            return opcion3;
        else
            return opcion4;

    }

    protected void setIcon(MenuItem item, boolean status, int dEnabled, int dDisabled) {

        if (status) {
            item.setIcon(getResources().getDrawable(dEnabled));
        } else {
            item.setIcon(getResources().getDrawable(dDisabled));
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_parar:
                analyticsFirebase.customEvents("Touch", "Match Pictograms", "Mute");
                gamesSettings.enableSound(gamesSettings.changeStatus(gamesSettings.isSoundOn()));
                sharedPrefsDefault.edit().putBoolean("muteSound", gamesSettings.isSoundOn()).apply();
                music.setMuted(gamesSettings.isSoundOn());
                setIcon(item, gamesSettings.isSoundOn(), R.drawable.ic_volume_off_white_24dp, R.drawable.ic_volume_up_white_24dp);
                return true;
            case R.id.check:
                analyticsFirebase.customEvents("Touch", "Match Pictograms", "Help Action");
                gamesSettings.enableHelpFunction(gamesSettings.changeStatus(gamesSettings.isHelpFunction()));
                sharedPrefsDefault.edit().putBoolean(getString(R.string.str_pistas), gamesSettings.isHelpFunction()).apply();
                setIcon(mMenu.getItem(1), gamesSettings.isHelpFunction(), R.drawable.ic_live_help_white_24dp, R.drawable.ic_unhelp);
                if (gamesSettings.isHelpFunction()) {
                    handlerHablar.postDelayed(animarHablar, 4000);
                    dialogo.mostrarFrase(getString(R.string.help_function));
                } else {
                    handlerHablar.removeCallbacks(animarHablar);
                    dialogo.mostrarFrase(getString(R.string.help_function_disabled));
                }
                return true;
            case R.id.score:
                analyticsFirebase.customEvents("Touch", "Match Pictograms", "Score Dialog");
                DialogGameProgressInform inform = new DialogGameProgressInform(this, R.layout.game_progress_score, game);
                inform.cargarDatosJuego();
                inform.showDialog();
                return true;
            case R.id.repeat:
                analyticsFirebase.customEvents("Touch", "Match Pictograms", "Repeat");
                gamesSettings.enableRepeatFunction(gamesSettings.changeStatus(gamesSettings.isRepeat()));
                sharedPrefsDefault.edit().putBoolean("repetir", gamesSettings.isRepeat()).apply();

                setIcon(item, gamesSettings.isRepeat(), R.drawable.ic_repeat_white_24dp, R.drawable.ic_unrepeat_ic_2);
                if (gamesSettings.isRepeat()) {
                    dialogo.mostrarFrase(getString(R.string.repeat_function_activated));
                } else {
                    dialogo.mostrarFrase(getString(R.string.repeat_function_disabled));
                }
                return true;
        }
        return false;
    }

    @Override
    protected void CorrectAction() {
        game.incrementCorrect();
        playCorrectSound();
        if (gamesSettings.isRepeatLection()) {
            model.setCorrectValue(GetPosicionLastButton(), 1);
            if (!verificarSiHayQueHacerReinicio()) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        decirPictoAleatorio();
                    }
                }, 2500);
            } else {
                lastButton = null;
                gamesSettings.changeRepeatLectionStatus();
                restartLection();
            }
        } else {
            model.setCorrectValue(lastPosicion, 1);
            lockPictogramOption(lastPosicion, lastButton);
            lastButton = null;
            lastPictogram = null;
            if (verificarSiHayQueHacerReinicio()) {
                if(gamesSettings.isRepeat())
                    gamesSettings.changeRepeatLectionStatus();
                cargarPuntos();
                game.incrementTimesRight();
                restartLection();
            }
        }
    }

    @Override
    protected void WrongAction(boolean isPictogram) {
        playWrongSong();
        game.incrementWrong();
        if (gamesSettings.isRepeatLection()) {
            animGameScore.animateCorrect(lastButton, game.getSmiley(Juego.DISSATISFIED));
            game.incrementWrong();
            playWrongSong();
        } else {
            useHappySound = false;
            playWrongSong();
            model.setCorrectValue(lastPosicion, 0);
            game.incrementWrong();
            setMenuScoreIcon();
            animGameScore.animateCorrect(lastButton, game.getSmiley(Juego.DISSATISFIED));
            if (isPictogram)
                lastPictogram = null;
            else
                lastButton = null;
        }
    }

    @Override
    protected void decirPictoAleatorio() {
        if (animarPicto != null && animarPicto.getVisibility() == View.VISIBLE)
            animarPicto.setVisibility(View.INVISIBLE);
        if (numeros.size() < model.getSize()) {
            int valor = model.elegirGanador();
            if (!numeros.contains(valor)) {
                numeros.add(valor);
                name = JSONutils.getNombre(pictogramas[valor],ConfigurarIdioma.getLanguaje());
                mTTS.getUtilsTTS().hablar(name);
            } else {
                decirPictoAleatorio();
            }
        }
    }

    public void loadLevePictograms() {
        numeros.clear();
        model.selectRandomPictogram(numeros);
        for (int i = 0; i < numeros.size(); i++) {
            Log.d(TAG, "loadLevePictograms: " + numeros.get(i));
            cargarTextoBoton(numeros.get(i), i);
        }
        numeros.clear();
        showGuessItem();
    }

    @Override
    protected void selectRandomOptions() {
        for (int i = 0; i < model.getSize(); i++) {
            selectRandomPictogram(i);
        }
    }


    public void loadModel() {
        switch (game.getGamelevel()) {
            case 0:
                model.setSize(2);
                break;
            case 1:
                model.setSize(3);
                break;
            case 2:
                model.setSize(4);
                break;
        }
        model.createArray();
    }

    @Override
    protected void lockPictogramOption(int opc, PictoView btn) {
        switch (opc) {
            case 0:
                animateWinnerPictogram(opcion1, btn);
                break;
            case 1:
                animateWinnerPictogram(opcion2, btn);
                break;
            case 2:
                animateWinnerPictogram(opcion3, btn);
                break;
            case 3:
                animateWinnerPictogram(opcion4, btn);
                break;
        }
    }

    public void enablePictogram(int position) {
        switch (position) {
            case 0:
                enablePictogram(opcion1, true);
                break;
            case 1:
                enablePictogram(opcion2, true);
                break;
            case 2:
                enablePictogram(opcion3, true);
                break;
            case 3:
                enablePictogram(opcion4, true);
                break;
        }
    }

    public void enableGuessOption(int position) {
        switch (position) {
            case 0:
                enablePictogram(guess1, false);
                break;
            case 1:
                enablePictogram(guess2, false);
                break;
            case 2:
                enablePictogram(guess3, false);
                break;
            case 3:
                enablePictogram(guess4, false);
                break;
        }
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

    public void selectOption(int option,PictoView pictoView){
        lastPosicion = option;
        lastPictogram = pictoView;
        speakOption(pictoView);
        makeClickOption(true);
    }

    public void selectGuessOption(PictoView pictoView){
        lastButton = pictoView;
        speakOption(pictoView);
        makeClickOption(false);
    }

}
