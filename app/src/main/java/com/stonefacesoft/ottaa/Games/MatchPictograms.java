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

import com.stonefacesoft.ottaa.Custom_Picto;
import com.stonefacesoft.ottaa.Dialogos.DialogGameProgressInform;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;

public class MatchPictograms extends GameViewSelectPictograms {


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
        setUpGame(1);
        selectRandomOptions();
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





    public void habilitarDesHabilitarBotones(Button button){
        button.setEnabled(!button.isEnabled());
    }

    protected void cargarPictogramas(){

    }


    protected void selectRandomPictogram(int pos) {
        int value=(int)Math.round((Math.random()*hijos.length()-1)+0);

        if(!numeros.contains(value)) {
            numeros.add(value);
            try {
                JSONObject object=hijos.getJSONObject(value);
                pictogramas[pos] = hijos.getJSONObject(value);
                if(!json.getNombre(pictogramas[pos]).toLowerCase().equals("error"))
                cargarOpcion(pos);
                else
                    selectRandomPictogram(pos);
            } catch (JSONException e) {
                e.printStackTrace();
                selectRandomPictogram(pos);
            }
        }else{
            selectRandomPictogram(pos);
        }
    }



    protected void cargarOpcion(int pos){
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

    protected void cargarValores(int pos) {
        int valor = (int) Math.round((Math.random() * 3 + 0));
        if (!numeros.contains(valor)) {
            numeros.add(valor);
            Log.d("Valor", "cargarValores: " + valor);
            cargarTextoBoton(valor, pos);

        } else {
            cargarValores(pos);
        }


    }



    protected void cargarTextoBoton(double valor,int pos){
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


    protected void esCorrecto(boolean esPicto) {
        if(lastPictogram!=null&&lastButton!=null) {
            try {

                if (lastPictogram.getCustom_Texto().equals(lastButton.getCustom_Texto())) {
                    game.incrementCorrect();
                    playCorrectSound();
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

    protected boolean verificarSiHayQueHacerReinicio(){
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
            selectRandomPictogram(0);
            selectRandomPictogram(1);
            selectRandomPictogram(2);
            selectRandomPictogram(3);
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



    protected void hacerClickOpcion(boolean esPicto){
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

    protected void speakOption(PictoView option){
        //este es el onclick del pictograma

        player.pauseAudio();
        if(!repetirLeccion) {
            mUtilsTTS.hablar(option.getCustom_Texto());
        }
    }
    protected void reiniciarLeccion(){
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

    protected void cargarPuntos(){
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

    protected int GetPosicionLastButton(){
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
    protected void onResume() {
        super.onResume();
        if(music!=null){
            music.stop();
            music.playMusic();
        }
        Json.getInstance().setmContext(this);
    }

    protected void drawImageAtPosition(){
        Drawable drawable=game.devolverCarita();
        drawable.setTint(getResources().getColor(R.color.colorWhite));
        mMenu.getItem(0).setIcon(drawable);
    }


    protected void animateGanador(Custom_Picto picto_ganador,int tipo){
        switch (tipo){
            case 0:
                selectButtonGanador(picto_ganador.getCustom_Texto()).startAnimation(AnimationUtils.loadAnimation(MatchPictograms.this, R.anim.shake));
                break;
            case 1:
                selectImagenGanadora(picto_ganador.getCustom_Texto()).startAnimation(AnimationUtils.loadAnimation(MatchPictograms.this, R.anim.shake));
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
}
