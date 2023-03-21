package com.stonefacesoft.ottaa.Games;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.firebase.perf.metrics.AddTrace;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.DialogGameProgressInform;
import com.stonefacesoft.ottaa.Games.Model.MemoryGameModelModel;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONException;

public class MemoryGame extends GameViewSelectPictograms {


    private final Handler resetOption = new Handler();
    private int foundPictos = 0;
    protected MemoryGameModelModel model;

    @AddTrace(name = "MemoryGame", enabled = true /* optional */)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(hijos.length()>4){
            showDescription(getString(R.string.memory_game));
            model = new MemoryGameModelModel();
            try {
                setUpGame(2,json.getId(mjJsonArrayTodosLosGrupos.getJSONObject(mPositionPadre)));
                game.setGamelevel(sharedPrefsDefault.getInt("MemoryGameLevel",0));
                game.setMaxLevel(3);
                game.setMaxStreak(30);
            }catch (JSONException e) {
                e.printStackTrace();
            }

            model = new MemoryGameModelModel();
            changeLevel();
            startGame();
            animGameScore = new AnimGameScore(this, mAnimationWin);
        }else{
            onBackPressed();
        }
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
                hideText(opcion1,pictogramas[0]);
                break;
            case 1:
                hideText(opcion2,pictogramas[1]);
                break;
            case 2:
                hideText(opcion2,pictogramas[2]);
                break;
            case 3:
                hideText(opcion3,pictogramas[3]);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Option1:
                selectOption(opcion1,0,0,0);
                break;
            case R.id.Option2:
                selectOption(opcion2,0,0,1);
                break;
            case R.id.Option3:
                selectOption(opcion3,0,0,2);
                break;
            case R.id.Option4:
                selectOption(opcion4,0,0,3);
                break;
            case R.id.Guess1:
                selectGuessOption(guess1,1,0);
                break;
            case R.id.Guess2:
                selectGuessOption(guess2,1,1);
                break;
            case R.id.Guess3:
                selectGuessOption(guess3,1,2);
                break;
            case R.id.Guess4:
                selectGuessOption(guess4,1,3);
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

    protected void animateWinnerPictogram(PictoView from, PictoView to) {

    }

    protected void makeClickOption(boolean esPicto) {
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

        player.stop();
        music.stop();
        Intent databack = new Intent();
        databack.putExtra("Boton", mPositionPadre);
        setResult(3, databack);
        if(game!=null) {
            sharedPrefsDefault.edit().putInt("MemoryGameLevel", game.getGamelevel()).apply();
            game.endUseTime();
            game.saveJsonObjects();
            game.uploadFirebaseGameData();
        }
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
                setIcon(item, gamesSettings.isSoundOn(), R.drawable.ic_volume_off_white_24dp, R.drawable.ic_volume_up_white_24dp);
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


    public void setOption(PictoView option, int row, int column) {
        int value;
        try{
            value = model.getMatrixIdPictogram()[row][column];
        }catch (Exception ex){
            value = -1;
        }
        if(value!=-1){
            try {
                option.setCustom_Texto(pictogramas[value].getJSONObject("texto").getString(ConfigurarIdioma.getLanguaje()));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            option.setVisibleText();
            option.setCustom_Img(json.getIcono(pictogramas[value]));
        }
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
        hidePictogramText(guess1);
        hidePictogramText(guess2);
        hidePictogramText(guess3);
        hidePictogramText(guess4);
        hidePictogramText(opcion1);
        hidePictogramText(opcion2);
        hidePictogramText(opcion3);
        hidePictogramText(opcion4);
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
        menu.getItem(1).setVisible(false);
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


    @Override
    protected void speakOption(PictoView option) {
        super.speakOption(option);
        mTTS.getUtilsTTS().hablar(option.getCustom_Texto());
    }

    @Override
    protected void lockPictogramOption(int opc, PictoView btn) {
        switch (opc){
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

    private void selectOption(PictoView option,int position,int row,int column){
        setOption(option, row, column);
        lastPosicion = position;
        isCorrect(option);
        speakOption(option);
    }

    private void selectGuessOption(PictoView pictoView,int row,int column){
        setOption(pictoView, row, column);
        isCorrect(pictoView);
        speakOption(pictoView);
    }

}
