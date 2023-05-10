package com.stonefacesoft.ottaa.utils.Accesibilidad.devices;

import android.content.Context;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;

import androidx.core.view.MotionEventCompat;

import com.stonefacesoft.ottaa.Games.GameSelector;
import com.stonefacesoft.ottaa.Games.TellAStory;
import com.stonefacesoft.ottaa.Views.MatchPictograms;
import com.stonefacesoft.ottaa.Games.WhichIsThePicto;
import com.stonefacesoft.ottaa.MainJuegos;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;

public class GameControl extends Controls{
    private MainJuegos mainJuegos;
    private WhichIsThePicto whichIsThePicto ;
    private MatchPictograms matchPictograms;
    private GameSelector gameSelector;
    private GameViewSelectPictograms gameViewSelectPictograms;
    private TellAStory tellAStory;
    private final String TAG="GameControl";


    public GameControl(MainJuegos game) {
        super(game);
        this.mainJuegos=game;
    }
    public GameControl(WhichIsThePicto whichIsThePicto){
        super(whichIsThePicto);
        this.whichIsThePicto=whichIsThePicto;
    }
    public GameControl(MatchPictograms matchPictograms){
        super(matchPictograms);
        this.matchPictograms=matchPictograms;
    }
    public GameControl(GameSelector gameSelector){
        super(gameSelector);
        this.gameSelector=gameSelector;
    }

    public GameControl(GameViewSelectPictograms gameSelector){
        super(gameSelector);
        this.gameViewSelectPictograms=gameSelector;
    }

    public GameControl( TellAStory gameSelector) {
        super(gameSelector);
        this.tellAStory = gameSelector;
    }

    @Override
    public boolean makeClick(MotionEvent event) {
        if (event.getSource() == InputDevice.SOURCE_MOUSE&&(getBarridoPantalla().isAvanzarYAceptar()||getBarridoPantalla().isSipAndPuff())) {
            switch (event.getButtonState()) {
                case MotionEvent.BUTTON_PRIMARY:
                    Log.e(TAG, "onTouchEvent: First Button");
                    makePrimaryClick(event);
                    return true;
                case MotionEvent.BUTTON_SECONDARY:
                    Log.e(TAG, "onTouchEvent: Secondary Button");
                    makeSecondaryClick(event);
                    return true;
                case MotionEvent.BUTTON_TERTIARY:
                    Log.e(TAG, "onTouchEvent: Tertiary Button");
                    makeTertiaryClick(event);
                    return true;

            }

        }else if(event.getSource()==InputDevice.SOURCE_MOUSE&&getBarridoPantalla().isScrollModeClicker()){
            switch (event.getButtonState()){
                case  MotionEvent.BUTTON_PRIMARY:
                    makeClick();
                    return true;
            }
        }else{
            final int action = MotionEventCompat.getActionMasked(event);
            switch (action){
                case MotionEvent.ACTION_MOVE:
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean makePrimaryClick(MotionEvent event) {
       getBarridoPantalla().avanzarBarrido();
        switch (preferences.getInt("deviceId",0)){
            case 0:
                if(getBarridoPantalla().isSipAndPuff())
                    selectSippAndPuff();
                break;
        }
        return true;
    }

    @Override
    public boolean makeSecondaryClick(MotionEvent event) {
        switch (preferences.getInt("deviceId",0)){
            case 0:
                getBarridoPantalla().volverAtrasBarrido();
                if(getBarridoPantalla().isSipAndPuff())
                    selectSippAndPuff();
                break;
            case 1:
                makeOnClick();
                break;
        }
        return true;
    }

    @Override
    public boolean makeTertiaryClick(MotionEvent event) {
          makeOnClick();
        return true;
    }

    private BarridoPantalla getBarridoPantalla(){
        if(mainJuegos!=null)
            return mainJuegos.getBarridoPantalla();
        else if(whichIsThePicto!=null)
            return whichIsThePicto.getBarridoPantalla();
        else if(matchPictograms!=null)
            return matchPictograms.getBarridoPantalla();
        else if(gameSelector!=null)
            return gameSelector.getBarridoPantalla();
        else if(gameViewSelectPictograms!=null)
            return gameViewSelectPictograms.getBarridoPantalla();
        else if(tellAStory!=null)
            return tellAStory.getBarridoPantalla();
        return null;
    }

    private void makeClick(){
        if(mainJuegos!=null)
             mainJuegos.OnClickBarrido();
        else if(whichIsThePicto!=null)
            whichIsThePicto.OnClickBarrido();
        else if(matchPictograms!=null)
             matchPictograms.OnClickBarrido();
        else if(gameSelector!=null)
             gameSelector.OnClickBarrido();
        else if(gameViewSelectPictograms!=null)
             gameViewSelectPictograms.OnClickBarrido();
        else if(tellAStory!=null)
            tellAStory.OnClickBarrido();
    }
    private void selectSippAndPuff(){
        if(mainJuegos!=null)
            mainJuegos.getFunction_scroll().HacerClickEnTiempo();
        else if(whichIsThePicto!=null)
            whichIsThePicto.OnClickBarrido();
        else if(matchPictograms!=null)
            matchPictograms.OnClickBarrido();
        else if(gameSelector!=null)
            gameSelector.OnClickBarrido();
        else if(gameViewSelectPictograms!=null)
            gameViewSelectPictograms.OnClickBarrido();
        else if(tellAStory!=null)
            tellAStory.OnClickBarrido();
    }
    private void  makeOnClick(){
        if(mainJuegos!=null)
            mainJuegos.onClick(getBarridoPantalla().getmListadoVistas().get(getBarridoPantalla().getPosicionBarrido()));
        else if(whichIsThePicto!=null)
            whichIsThePicto.onClick(getBarridoPantalla().getmListadoVistas().get(getBarridoPantalla().getPosicionBarrido()));
        else if(matchPictograms!=null)
            matchPictograms.onClick(getBarridoPantalla().getmListadoVistas().get(getBarridoPantalla().getPosicionBarrido()));
        else if(gameSelector!=null)
            gameSelector.onClick(getBarridoPantalla().getmListadoVistas().get(getBarridoPantalla().getPosicionBarrido()));
        else if(gameViewSelectPictograms!=null)
            gameViewSelectPictograms.onClick(getBarridoPantalla().getmListadoVistas().get(getBarridoPantalla().getPosicionBarrido()));
        else if(tellAStory!=null)
            tellAStory.onClick(getBarridoPantalla().getmListadoVistas().get(getBarridoPantalla().getPosicionBarrido()));
    }
    @Override
    public boolean pressBackButton() {
        makeClick();
        return true;
    }

}
