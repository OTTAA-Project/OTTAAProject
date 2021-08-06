package com.stonefacesoft.ottaa.Games;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.Dialogos.DialogGameProgressInform;
import com.stonefacesoft.ottaa.Games.Model.MemoryGameModelModel;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONException;

public class MemoryGame extends GameViewSelectPictograms {


    private final Handler resetOption = new Handler();
    private int foundPictos = 0;
    protected MemoryGameModelModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDescription(getString(R.string.memory_game));
        model = new MemoryGameModelModel();
        setUpGame(2);
        game.setGamelevel(sharedPrefsDefault.getInt("MemoryGameLevel",0));
        game.setMaxLevel(3);
        game.setMaxStreak(30);
        model = new MemoryGameModelModel();
        changeLevel();
        startGame();
        animGameScore = new AnimGameScore(this, mAnimationWin);
    }

    @Override
    protected void selectRandomPictogram(int pos) {
        int value = (int) Math.round((Math.random() * hijos.length() - 1) + 0);
        if (!numeros.contains(value)) {
            numeros.add(value);
            try {
                pictogramas[pos] = hijos.getJSONObject(value);
            } catch (JSONException e) {https://www.google.com/search?q=terryfi&oq=terryfi&aqs=chrome..69i57.1869j0j7&sourceid=chrome&ie=UTF-8
                e.printStackTrace();
                selectRandomPictogram(pos);
            }
        } else {
            selectRandomPictogram(pos);
        }
    }

    @Override
    protected void selectRandomOptions() {
        for (int i = 0; i <model.getSize() ; i++) {
            selectRandomPictogram(i);
        }
    }

    protected void cargarOpcion(int pos) {
        switch (pos) {
            case 0:
                opcion1.setCustom_Img(json.getIcono(pictogramas[0]));
                opcion1.setCustom_Texto(JSONutils.getNombre(pictogramas[0],sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));
                opcion1.setInvisibleCustomTexto();
                break;
            case 1:
                opcion2.setCustom_Img(json.getIcono(pictogramas[1]));
                opcion2.setCustom_Texto(JSONutils.getNombre(pictogramas[1],sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));
                opcion1.setInvisibleCustomTexto();
                break;
            case 2:

                opcion3.setCustom_Img(json.getIcono(pictogramas[2]));
                opcion3.setCustom_Texto(JSONutils.getNombre(pictogramas[2],sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));
                opcion1.setInvisibleCustomTexto();
                break;
            case 3:
                opcion4.setCustom_Img(json.getIcono(pictogramas[3]));
                opcion4.setCustom_Texto(JSONutils.getNombre(pictogramas[3],sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));
                opcion1.setInvisibleCustomTexto();
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Option1:

                setOption(opcion1, 0, 0);
                lastPosicion = 0;
                isCorrect(opcion1);
                speakOption(opcion1);
                break;
            case R.id.Option2:

                setOption(opcion2, 0, 1);
                lastPosicion = 1;
                isCorrect(opcion2);
                speakOption(opcion2);
                //hacerClickOpcion(true);
                break;
            case R.id.Option3:
                setOption(opcion3, 0, 2);
                lastPosicion = 2;
                isCorrect(opcion3);
                speakOption(opcion3);
                //hacerClickOpcion(true);
                break;
            case R.id.Option4:
                setOption(opcion4, 0, 3);
                lastPosicion = 3;
                isCorrect(opcion4);
                speakOption(opcion4);
                //hacerClickOpcion(true);
                break;
            case R.id.Guess1:
                setOption(guess1, 1, 0);
                isCorrect(guess1);
                speakOption(guess1);
                //hacerClickOpcion(false);
                break;
            case R.id.Guess2:
                setOption(guess2, 1, 1);
                isCorrect(guess2);
                speakOption(guess2);
                //hacerClickOpcion(false);
                break;
            case R.id.Guess3:
                setOption(guess3, 1, 2);
                isCorrect(guess3);
                speakOption(guess3);
                //hacerClickOpcion(false);
                break;
            case R.id.Guess4:
                setOption(guess4, 1, 3);
                isCorrect(guess4);
                speakOption(guess4);
                //hacerClickOpcion(false);
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

    protected void animarPictoGanador(PictoView from, PictoView to) {

    }

    protected void hacerClickOpcion(boolean esPicto) {
        if (gamesSettings.isHelpFunction())
            handlerHablar.postDelayed(animarHablar, 1000);

    }

    @Override
    protected void esCorrecto(boolean esPicto) {
        super.esCorrecto(esPicto);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sharedPrefsDefault.edit().putInt("MemoryGameLevel",game.getGamelevel()).apply();
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_parar:
                analyticsFirebase.customEvents("Touch", "Memory Game", "Mute");
                gamesSettings.enableSound(gamesSettings.changeStatus(gamesSettings.isSoundOn()));
                sharedPrefsDefault.edit().putBoolean("muteSound", gamesSettings.isSoundOn()).apply();
                music.setMuted(gamesSettings.isSoundOn());
                setIcon(item, gamesSettings.isSoundOn(), R.drawable.ic_baseline_volume_off_24, R.drawable.ic_baseline_volume_up_24);
                return true;
            case R.id.check:
                analyticsFirebase.customEvents("Touch", "Memory Game", "Help Action");
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
                analyticsFirebase.customEvents("Touch", "Memory Game", "Score Dialog");
                DialogGameProgressInform inform = new DialogGameProgressInform(this, R.layout.game_progress_score, game);
                inform.cargarDatosJuego();
                inform.showDialog();
                return true;
            case R.id.repeat:
                analyticsFirebase.customEvents("Touch", "Memory Game", "Repeat");
                gamesSettings.enableRepeatFunction(gamesSettings.changeStatus(gamesSettings.isRepeat()));
                sharedPrefsDefault.edit().putBoolean("repetir", gamesSettings.isHelpFunction()).apply();
                setIcon(item, gamesSettings.isHelpFunction(), R.drawable.ic_repeat_white_24dp, R.drawable.ic_unrepeat_ic_2);
                if (gamesSettings.isHelpFunction()) {
                    dialogo.mostrarFrase(getString(R.string.repeat_function_activated));
                } else {
                    dialogo.mostrarFrase(getString(R.string.repeat_function_disabled));
                }
                return true;
        }
        return false;
    }


    /**
     *
     */


    public void setOption(PictoView option, int row, int column) {
        try {
            Log.d("TAG", "setOption: " + pictogramas[model.getMatrixIdPictogram()[row][column]].toString());
            option.setCustom_Texto(pictogramas[model.getMatrixIdPictogram()[row][column]].getJSONObject("texto").getString(ConfigurarIdioma.getLanguaje()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        option.setVisibleText();
        option.setCustom_Img(json.getIcono(pictogramas[model.getMatrixIdPictogram()[row][column]]));
    }


    @Override
    protected void isCorrect(PictoView view) {

        if (lastButton == null && lastPictogram != null) {
            if (!lastPictogram.getTag().toString().equals(view.getTag().toString()))
                selectLastButton(view);
        } else if (lastButton == null && lastPictogram == null) {
            selectLastPictogram(view);
        }
        if (lastButton != null && lastPictogram != null) {
            lockOptions();
            //If texts are equal, means that the user was correct
            if (lastPictogram.getCustom_Texto().contentEquals(lastButton.getCustom_Texto())) {
                playCorrectSound();
                CorrectAction();
            } else {
                playWrongSong();
                WrongAction();
            }
        }
    }


    private void startGame() {
        numeros.clear();
        foundPictos = 0;
        model.refresMatrix();
        model.resetMatrix();
        model.resetHistory();
        selectRandomOptions();
        model.addRandomIndex();
        setGuessDrawable(guess1);
        setGuessDrawable(guess2);
        setGuessDrawable(guess3);
        setGuessDrawable(guess4);
        setGuessDrawable(opcion1);
        setGuessDrawable(opcion2);
        setGuessDrawable(opcion3);
        setGuessDrawable(opcion4);
        setInvisibleText(guess1);
        setInvisibleText(guess2);
        setInvisibleText(guess3);
        setInvisibleText(guess4);
        setInvisibleText(opcion1);
        setInvisibleText(opcion2);
        setInvisibleText(opcion3);
        setInvisibleText(opcion4);
        showGuessItem();
    }

    @Override
    protected void lockOptions() {
        super.lockOptions();
        opcion1.setEnabled(false);
        opcion2.setEnabled(false);
        opcion3.setEnabled(false);
        opcion4.setEnabled(false);
        guess1.setEnabled(false);
        guess2.setEnabled(false);
        guess3.setEnabled(false);
        guess4.setEnabled(false);

    }

    @Override
    protected void unlockOptions() {
        super.unlockOptions();
        opcion1.setEnabled(true);
        opcion2.setEnabled(true);
        opcion3.setEnabled(true);
        opcion4.setEnabled(true);
        guess1.setEnabled(true);
        guess2.setEnabled(true);
        guess3.setEnabled(true);
        guess4.setEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.getItem(2).setVisible(false);
        return true;
    }

    @Override
    protected void CorrectAction() {
        game.incrementCorrect();
        game.incrementTimesRight();
        unlockOptions();
        model.addHistoryValue(foundPictos,lastButton.getCustom_Texto());
        foundPictos++;
        setMenuScoreIcon();
        animateChildrens(lastButton, lastPictogram, true);
        lastPictogram = null;
        lastButton = null;
        if (foundPictos == model.getSize()) {
            resetOption.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(game.isChangeLevel()){
                        game.changeLevelGame();
                        changeLevel();
                    }
                    startGame();
                }
            }, 2500);
        }
    }

    @Override
    protected void WrongAction() {
        game.incrementWrong();
        setMenuScoreIcon();
        animateChildrens(findViewById(lastPictogram.getId()), findViewById(lastButton.getId()), false);
        resetOption.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lastPictogram != null && lastButton != null) {
                    lastPictogram.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
                    lastButton.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
                    lastPictogram.setInvisibleCustomTexto();
                    lastButton.setInvisibleCustomTexto();
                    lastPictogram = null;
                    lastButton = null;
                    unlockOptions();
                }
            }
        }, 2500);
    }

    private void selectLastButton(PictoView view) {
        lastButton = view;
        if (model.theViewHasBeenSelected(lastButton.getCustom_Texto()))
            lastButton = null;
    }

    private void selectLastPictogram(PictoView view) {
        lastPictogram = view;
        if (model.theViewHasBeenSelected(lastPictogram.getCustom_Texto()))
            lastPictogram = null;
    }

    private void animateChildrens(PictoView view1, PictoView view2, boolean isCorrect) {
        if (isCorrect) {
            animGameScore.animateCorrect(view1, game.getSmiley(Juego.VERY_SSATISFIED), mAnimationWin);
            animGameScore.animateCorrect(view2, game.getSmiley(Juego.VERY_SSATISFIED), mAnimationWin2);
        } else {
            animGameScore.animateCorrect(view1, game.getSmiley(Juego.DISSATISFIED), mAnimationWin);
            animGameScore.animateCorrect(view2, game.getSmiley(Juego.DISSATISFIED), mAnimationWin2);
        }
    }

    public void changeLevel(){
        switch (game.getGamelevel()){
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

    public void showGuessItem(){
        switch (game.getGamelevel()){
            case 0:
                opcion3.setVisibility(View.INVISIBLE);
                opcion4.setVisibility(View.INVISIBLE);
                guess3.setVisibility(View.INVISIBLE);
                guess4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                opcion3.setVisibility(View.VISIBLE);
                guess3.setVisibility(View.VISIBLE);
                opcion4.setVisibility(View.INVISIBLE);
                guess4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                opcion4.setVisibility(View.VISIBLE);
                guess4.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void speakOption(PictoView option) {
        super.speakOption(option);
        mUtilsTTS.hablar(option.getCustom_Texto());
    }

    @Override
    protected void bloquearOpcionPictograma(int opc, PictoView btn) {
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
}
