package com.stonefacesoft.ottaa.Games;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.Dialogos.DialogGameProgressInform;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONException;
import org.json.JSONObject;

public class MemoryGame extends GameViewSelectPictograms {

    private int matrixIdPictogram[][]= new int[2][4];
    private int controlIndexSelect[]=new int[4];// se utiliza para indicar que pictogramas fueron seleccionados dos veces
    private Handler resetOption=new Handler();
    private int foundPictos=0;
    private String history[]=new String[4];




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDescription(getString(R.string.memory_game));
        setUpGame(2);
        startGame();
        animGameScore = new AnimGameScore(this, mAnimationWin);
    }

    @Override
    protected void selectRandomPictogram(int pos) {
        int value=(int)Math.round((Math.random()*hijos.length()-1)+0);
        if(!numeros.contains(value)) {
            numeros.add(value);
            try {
                JSONObject object=hijos.getJSONObject(value);
                pictogramas[pos] = hijos.getJSONObject(value);
            } catch (JSONException e) {
                e.printStackTrace();
                selectRandomPictogram(pos);
            }
        }else{
            selectRandomPictogram(pos);
        }
    }

    @Override
    protected void selectRandomOptions() {
        selectRandomPictogram(0);
        selectRandomPictogram(1);
        selectRandomPictogram(2);
        selectRandomPictogram(3);
    }

        protected void cargarOpcion(int pos){
        switch (pos){
            case 0:
                opcion1.setCustom_Img(json.getIcono(pictogramas[0]));
                opcion1.setCustom_Texto(json.getNombre(pictogramas[0]));
                opcion1.setInvisibleCustomTexto();
                break;
            case 1:
                opcion2.setCustom_Img(json.getIcono(pictogramas[1]));
                opcion2.setCustom_Texto(json.getNombre(pictogramas[1]));
                opcion1.setInvisibleCustomTexto();
                break;
            case 2:

                opcion3.setCustom_Img(json.getIcono(pictogramas[2]));
                opcion3.setCustom_Texto(json.getNombre(pictogramas[2]));
                opcion1.setInvisibleCustomTexto();
                break;
            case 3:
                opcion4.setCustom_Img(json.getIcono(pictogramas[3]));
                opcion4.setCustom_Texto(json.getNombre(pictogramas[3]));
                opcion1.setInvisibleCustomTexto();
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Option1:

                setOption(opcion1,0,0);
                lastPosicion=0;
                isCorrect(opcion1);
                speakOption(opcion1);
                break;
            case R.id.Option2:

                setOption(opcion2,0,1);
                lastPosicion=1;
                isCorrect(opcion2);
                speakOption(opcion2);
                //hacerClickOpcion(true);
                break;
            case R.id.Option3:
                setOption(opcion3,0,2);
                lastPosicion=2;
                isCorrect(opcion3);
                speakOption(opcion3);
                //hacerClickOpcion(true);
                break;
            case R.id.Option4:
                setOption(opcion4,0,3);
                lastPosicion=3;
                isCorrect(opcion4);
                speakOption(opcion4);
                //hacerClickOpcion(true);
                break;
            case R.id.Guess1:
                setOption(guess1,1,0);
                isCorrect(guess1);
                speakOption(guess1);
                //hacerClickOpcion(false);
                break;
            case R.id.Guess2:
                setOption(guess2,1,1);
                isCorrect(guess2);
                speakOption(guess2);
                //hacerClickOpcion(false);
                break;
            case R.id.Guess3:
                setOption(guess3,1,2);
                isCorrect(guess3);
                speakOption(guess3);
                //hacerClickOpcion(false);
                break;
            case R.id.Guess4:
                setOption(guess4,1,3);
                isCorrect(guess4);
                speakOption(guess4);
                //hacerClickOpcion(false);
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

    protected void animarPictoGanador(PictoView from, PictoView to) {

    }
    protected void hacerClickOpcion(boolean esPicto){
        if(isChecked)
            handlerHablar.postDelayed(animarHablar,1000);

    }

    @Override
    protected void esCorrecto(boolean esPicto) {
        super.esCorrecto(esPicto);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_parar:
                analyticsFirebase.customEvents("Touch", "Memory Game", "Mute");
                mute = !mute;
                sharedPrefsDefault.edit().putBoolean("muteSound", mute).apply();
                music.setMuted(mute);
                setIcon(item, mute, R.drawable.ic_volume_off_white_24dp, R.drawable.ic_volume_up_white_24dp);
                return true;
            case R.id.check:
                analyticsFirebase.customEvents("Touch","Match Pictograms","Help Action");
                isChecked=!isChecked;
                sharedPrefsDefault.edit().putBoolean(getString(R.string.str_pistas), isChecked).apply();
                setIcon(mMenu.getItem(1), isChecked, R.drawable.ic_live_help_white_24dp, R.drawable.ic_unhelp);
                if (isChecked) {
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
                isRepeatlection = !isRepeatlection;
                sharedPrefsDefault.edit().putBoolean("repetir", isRepeatlection).apply();

                setIcon(item, isRepeatlection, R.drawable.ic_repeat_white_24dp, R.drawable.ic_unrepeat_ic_2);
                if (isRepeatlection) {
                    dialogo.mostrarFrase(getString(R.string.repeat_function_activated));
                } else {
                    dialogo.mostrarFrase(getString(R.string.repeat_function_disabled));
                }
                return true;
        }
        return false;
    }

    public void resetMatrix(){
        for (int i = 0; i < 2 ; i++) {
            for (int j = 0; j < 4; j++) {
                matrixIdPictogram[i][j]=-1;
            }
        }
    }

    public void resetValue(){
        for (int i = 0; i <4 ; i++) {
            controlIndexSelect[i]=0;
        }
    }

    public void resetHistory(){
        for (int i = 0; i <history.length ; i++) {
            history[i]="";
        }
    }
/**
 *
 * */
    public void addRandomIndex(){
        for (int i = 0; i < matrixIdPictogram.length ; i++) {// recorro la filas
            for (int j = 0; j < matrixIdPictogram[i].length; j++) { //recorro las columnas
                if(matrixIdPictogram[i][j]==-1){
                    int index=(int)(Math.random()*4)+0; // picto a seleccionar
                    if(controlIndexSelect[index]<2){ //pregunto cuantas veces fue seleccionado el valor
                        matrixIdPictogram[i][j]=index;
                        controlIndexSelect[index]++; //le indico que ese valor fue seleccionado
                    }else{
                        addRandomIndex(); //
                    }

                }
            }
        }
    }

    public void setOption(PictoView option,int row,int column){

        Log.d("TAG", "setOption: "+pictogramas[matrixIdPictogram[row][column]].toString());
            try {
                option.setCustom_Texto(pictogramas[matrixIdPictogram[row][column]].getJSONObject("texto").getString(json.getIdioma()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        option.setVisibleText();
        option.setCustom_Img(json.getIcono(pictogramas[matrixIdPictogram[row][column]]));
    }


    @Override
    protected void isCorrect(PictoView view) {

        if (lastButton == null && lastPictogram != null) {
            selectLastButton(view);
        } else if (lastButton == null && lastPictogram == null) {
            selectLastPictogram(view);
        }
        if (lastButton != null && lastPictogram != null) {
            lockOptions();
            //If texts are equal, means that the user was correct
            if (lastPictogram.getCustom_Texto().contentEquals(lastButton.getCustom_Texto())) {

                CorrectAction();
            } else {
                WrongAction();
            }
        }
    }


    private void startGame(){
        numeros.clear();
        foundPictos =0;
        resetMatrix();
        resetValue();
        resetHistory();
        selectRandomOptions();
        addRandomIndex();
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

    private boolean theViewHasBeenSelected(String text) {
        for (int i = 0; i < history.length; i++) {
            if (history[i].contentEquals(text))
                return true;
        }
        return false;
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
        playCorrectSound();
        player.playYesSound();
        player.playYupi1Sound();
        unlockOptions();
        history[foundPictos] = lastButton.getCustom_Texto();
        foundPictos++;
        setMenuScoreIcon();
        animateChildrens(lastButton,lastPictogram,true);
        lastPictogram = null;
        lastButton = null;
        if (foundPictos == pictogramas.length) {
            resetOption.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startGame();
                }
            }, 2500);
        }
    }

    @Override
    protected void WrongAction() {
        player.playOhOhSound();
        game.incrementWrong();
        setMenuScoreIcon();
        animateChildrens(findViewById(lastPictogram.getId()),findViewById(lastButton.getId()),false);
        resetOption.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastPictogram.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
                lastButton.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
                lastPictogram.setInvisibleCustomTexto();
                lastButton.setInvisibleCustomTexto();
                lastPictogram = null;
                lastButton = null;
                unlockOptions();
            }
        }, 2500);
    }

    private void selectLastButton(PictoView view){
        lastButton = view;
        if (theViewHasBeenSelected(lastButton.getCustom_Texto()))
            lastButton = null;
    }

    private void selectLastPictogram(PictoView view){
        lastPictogram = view;
        if (theViewHasBeenSelected(lastPictogram.getCustom_Texto()))
            lastPictogram = null;
    }

    private void animateChildrens(PictoView view1,PictoView view2,boolean isCorrect){
        if(isCorrect){
            animGameScore.animateCorrect(view1, game.getSmiley(Juego.VERY_SSATISFIED),mAnimationWin);
            animGameScore.animateCorrect(view2, game.getSmiley(Juego.VERY_SSATISFIED),mAnimationWin2);
        }else{
            animGameScore.animateCorrect(view1, game.getSmiley(Juego.DISSATISFIED),mAnimationWin);
            animGameScore.animateCorrect(view2, game.getSmiley(Juego.DISSATISFIED),mAnimationWin2);
        }
    }
}
