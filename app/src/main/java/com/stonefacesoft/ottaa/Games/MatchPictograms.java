package com.stonefacesoft.ottaa.Games;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.Custom_Picto;
import com.stonefacesoft.ottaa.Dialogos.DialogGameProgressInform;
import com.stonefacesoft.ottaa.Games.Model.MatchPictogramsModel;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
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
    private boolean repetirLeccion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDescription(getString(R.string.join_pictograms));
        model = new MatchPictogramsModel();
        setUpGame(1);
        game.setGamelevel(1);
        loadModel();
        selectRandomOptions();
        numeros.clear();
        loadLevePictograms();
        numeros.clear();
        guess1.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
        guess2.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
        guess3.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
        guess4.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
        animGameScore = new AnimGameScore(this, mAnimationWin);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Option1:
                lastPosicion = 0;
                lastPictogram = opcion1;
                speakOption(opcion1);
                hacerClickOpcion(true);
                break;
            case R.id.Option2:
                lastPosicion = 1;
                lastPictogram = opcion2;
                speakOption(opcion2);
                hacerClickOpcion(true);
                break;
            case R.id.Option3:
                lastPosicion = 2;
                lastPictogram = opcion3;
                speakOption(opcion3);
                hacerClickOpcion(true);
                break;
            case R.id.Option4:
                lastPosicion = 3;
                lastPictogram = opcion4;
                speakOption(opcion4);
                hacerClickOpcion(true);
                break;
            case R.id.Guess1:
                lastButton = guess1;
                speakOption(guess1);
                hacerClickOpcion(false);
                break;
            case R.id.Guess2:
                lastButton = guess2;
                speakOption(guess2);
                hacerClickOpcion(false);
                break;
            case R.id.Guess3:
                lastButton = guess3;
                speakOption(guess3);
                hacerClickOpcion(false);
                break;
            case R.id.Guess4:
                lastButton = guess4;
                speakOption(guess4);
                hacerClickOpcion(false);
                break;
            case R.id.action_parar:
                gamesSettings.enableSound(gamesSettings.changeStatus(gamesSettings.isSoundOn()));
                music.setMuted(gamesSettings.isSoundOn());
                sharedPrefsDefault.edit().putBoolean("muteSound", gamesSettings.isSoundOn()).apply();
//                    if(mute)
//                        sound_on_off.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_white_24dp));
//                    else
//                        sound_on_off.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
                break;
            case R.id.btnBarrido:
                onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        }
    }


    public void habilitarDesHabilitarBotones(Button button) {
        button.setEnabled(!button.isEnabled());
    }

    protected void cargarPictogramas() {

    }


    protected void selectRandomPictogram(int pos) {
        int value = (int) Math.round((Math.random() * hijos.length() - 1) + 0);
        if (!numeros.contains(value)) {
            numeros.add(value);
            try {
                pictogramas[pos] = hijos.getJSONObject(value);
                if (!json.getNombre(pictogramas[pos]).toLowerCase().equals("error"))
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
                loadData(opcion1, json.getNombre(pictogramas[pos]), json.getIcono(pictogramas[pos]), true);
                break;
            case 1:
                loadData(opcion2, json.getNombre(pictogramas[pos]), json.getIcono(pictogramas[pos]), true);
                break;
            case 2:
                loadData(opcion3, json.getNombre(pictogramas[pos]), json.getIcono(pictogramas[pos]), true);

                break;
            case 3:
                loadData(opcion4, json.getNombre(pictogramas[pos]), json.getIcono(pictogramas[pos]), true);
                break;
        }

    }

    protected void cargarValores(int pos) {


    }


    protected void cargarTextoBoton(double valor, int pos) {
        switch (pos) {
            case 0:

                loadData(guess1, json.getNombre(pictogramas[(int) valor]), json.getIcono(pictogramas[(int) valor]), false);
                break;
            case 1:
                loadData(guess2, json.getNombre(pictogramas[(int) valor]), json.getIcono(pictogramas[(int) valor]), false);
                break;
            case 2:
                loadData(guess3, json.getNombre(pictogramas[(int) valor]), json.getIcono(pictogramas[(int) valor]), false);
                break;
            case 3:
                loadData(guess4, json.getNombre(pictogramas[(int) valor]), json.getIcono(pictogramas[(int) valor]), false);
                break;
        }
    }


    protected void esCorrecto(boolean esPicto) {
        if (repetirLeccion) {
            if (lastButton.getCustom_Texto().equals(name)) {
                CorrectAction();
            }else{
                WrongAction(esPicto);
            }
        } else {
            if (lastPictogram != null && lastButton != null) {

                try {
                    if (lastPictogram.getCustom_Texto().equals(lastButton.getCustom_Texto())) {
                        CorrectAction();
                    } else {
                        WrongAction(esPicto);
                    }
                    if (verificarSiHayQueHacerReinicio()) {
                        if (gamesSettings.isRepeat()) {
                            repetirLeccion = !repetirLeccion;
                        }
                        cargarPuntos();
                        reiniciarLeccion();
                    }
                } catch (Exception ex) {

                }
            }
        }
    }

    protected boolean verificarSiHayQueHacerReinicio() {
        return model.restartValue();
    }


    public void reiniciar() {
        pauseAudio();
        habilitarPictoGrama(guess1, false);
        habilitarPictoGrama(guess2, false);
        habilitarPictoGrama(guess3, false);
        habilitarPictoGrama(guess4, false);
        if (repetirLeccion) {
            mUtilsTTS.hablarConDialogo(getString(R.string.repeat_pictograms));
            opcion1.setVisibility(View.INVISIBLE);
            opcion2.setVisibility(View.INVISIBLE);
            opcion3.setVisibility(View.INVISIBLE);
            opcion4.setVisibility(View.INVISIBLE);
        }

        if (!repetirLeccion) {
            guess1.setVisibility(View.VISIBLE);
            guess2.setVisibility(View.VISIBLE);
            guess3.setVisibility(View.VISIBLE);
            guess4.setVisibility(View.VISIBLE);
            guess1.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess2.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess3.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            guess4.setCustom_Img(getDrawable(R.drawable.ic_help_outline_black_24dp));
            numeros.clear();
            habilitarPictoGrama(opcion1, true);
            habilitarPictoGrama(opcion2, true);
            habilitarPictoGrama(opcion3, true);
            habilitarPictoGrama(opcion4, true);
            for (int i = 0; i < model.getSize(); i++) {
                selectRandomPictogram(i);
            }
            numeros.clear();
            loadLevePictograms();
        } else {
            numeros.clear();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    decirPictoAleatorio();
                }
            }, 5000);

        }
        model.refreshValueIndex();
    }


    protected void hacerClickOpcion(boolean esPicto) {
        if (gamesSettings.isHelpFunction())
            handlerHablar.postDelayed(animarHablar, 1000);
        if (lastButton != null && lastPictogram != null) {
            if (!repetirLeccion)
                esCorrecto(esPicto);
        } else if (repetirLeccion) {
            esCorrecto(esPicto);
        }
    }

    protected void speakOption(PictoView option) {
        super.speakOption(option);
        if (!repetirLeccion) {
            mUtilsTTS.hablar(option.getCustom_Texto());
        }
    }

    protected void reiniciarLeccion() {
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

    protected void cargarPuntos() {
        game.getScoreClass().calcularValor();
        drawImageAtPosition();
    }

    protected void animarPictoGanador(PictoView from, PictoView to) {

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

    protected void animarPictoReset(Custom_Picto picto) {

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0);
        animation.setRepeatMode(0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        picto.startAnimation(animation);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //la variable temporal galeria grupos se la usa para modificar puntaje


        stopAudio();
        Intent databack = new Intent();
        databack.putExtra("Boton", mPositionPadre);
        setResult(3, databack);
        game.endUseTime();
        game.guardarObjetoJson();
        game.subirDatosJuegosFirebase();
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
        Drawable drawable = game.devolverCarita();
        drawable.setTint(getResources().getColor(R.color.colorWhite));
        mMenu.getItem(0).setIcon(drawable);
    }


    protected void animateGanador(Custom_Picto picto_ganador, int tipo) {
        switch (tipo) {
            case 0:
                selectButtonGanador(picto_ganador.getCustom_Texto()).startAnimation(AnimationUtils.loadAnimation(MatchPictograms.this, R.anim.shake));
                break;
            case 1:
                selectImagenGanadora(picto_ganador.getCustom_Texto()).startAnimation(AnimationUtils.loadAnimation(MatchPictograms.this, R.anim.shake));
                break;
        }
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
        if (repetirLeccion) {
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
                if (gamesSettings.isRepeat()) {
                    repetirLeccion = !repetirLeccion;
                }
                cargarPuntos();
                reiniciar();
            }
        } else {

            lastPictogram.setVisibleText();
            if (!useHappySound) {
                useHappySound = true;
            }
            model.setCorrectValue(lastPosicion, 1);
            animarPictoGanador(lastButton,lastPictogram);
            bloquearOpcionPictograma(lastPosicion,lastButton);
            lastButton = null;
            lastPictogram = null;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //btn.setVisibility(View.INVISIBLE);
                    decirPictoAleatorio();
                }
            }, 2500);

        }

    }

    @Override
    protected void WrongAction(boolean isPictogram) {
       if(repetirLeccion){
           animGameScore.animateCorrect(lastButton, game.getSmiley(Juego.DISSATISFIED));
           game.incrementWrong();
           playWrongSong();
       }else{
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
                name = json.getNombre(pictogramas[valor]);
                mUtilsTTS.hablar(name);
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
        if(game.getGamelevel()>=0){
            opcion3.setVisibility(View.INVISIBLE);
            opcion4.setVisibility(View.INVISIBLE);
            guess3.setVisibility(View.INVISIBLE);
            guess4.setVisibility(View.INVISIBLE);
        }
        if(game.getGamelevel()>=1){
            opcion3.setVisibility(View.VISIBLE);
            guess3.setVisibility(View.VISIBLE);
        }
        if(game.getGamelevel()>=2){
            opcion4.setVisibility(View.VISIBLE);
            guess4.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void selectRandomOptions() {
        for (int i = 0; i < model.getSize(); i++) {
            selectRandomPictogram(i);
        }
    }

    public void loadData(PictoView view, String name, Drawable drawable, boolean loadDrawable) {
        view.setCustom_Texto(name);
        if (loadDrawable) {
            view.setCustom_Img(drawable);
            view.setInvisibleCustomTexto();
        }
    }

    @Override
    protected void bloquearOpcionPictograma(int opc, PictoView btn) {
        super.bloquearOpcionPictograma(opc, btn);
    }

    public void loadModel(){
        int size =2 ;
        switch (game.getGamelevel()){
            case 0:
                size = 2;
                break;
            case 1:
                size = 3;
                break;
            case 2:
                size = 4;
                break;
        }
        model.setSize(size);
        model.refreshValueIndex();
    }
}
