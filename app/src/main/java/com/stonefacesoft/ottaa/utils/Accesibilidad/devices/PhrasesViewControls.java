package com.stonefacesoft.ottaa.utils.Accesibilidad.devices;

import android.content.Context;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;

import androidx.core.view.MotionEventCompat;

import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;

public class PhrasesViewControls extends Controls {
    private final PhrasesView phrasesView;
    private final String TAG ="PhraseViewControls";
    public PhrasesViewControls(PhrasesView phrasesView) {
        super(phrasesView);
        this.phrasesView = phrasesView;
    }

    @Override
    public boolean makeClick(MotionEvent event) {
        if (event.getSource() == InputDevice.SOURCE_MOUSE&&(phrasesView.getBarridoPantalla().isAvanzarYAceptar()||phrasesView.getBarridoPantalla().isSipAndPuff())) {
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

        }else if(event.getSource()==InputDevice.SOURCE_MOUSE&&phrasesView.getBarridoPantalla().isScrollModeClicker()){
            switch (event.getButtonState()){
                case  MotionEvent.BUTTON_PRIMARY:
                    phrasesView.OnClickBarrido();
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
        phrasesView.getBarridoPantalla().avanzarBarrido();
        switch (preferences.getInt("deviceId",0)){
            case 0:
                if(phrasesView.getBarridoPantalla().isSipAndPuff())
                    phrasesView.getFunction_scroll().HacerClickEnTiempo();
                break;
        }
        return true;
    }

    @Override
    public boolean makeSecondaryClick(MotionEvent event) {
        switch (preferences.getInt("deviceId",0)){
            case 0:
                phrasesView.getBarridoPantalla().volverAtrasBarrido();
                if(phrasesView.getBarridoPantalla().isSipAndPuff())
                    phrasesView.getFunction_scroll().HacerClickEnTiempo();
                break;
            case 1:
                phrasesView.onClick(phrasesView.getBarridoPantalla().getmListadoVistas().get(phrasesView.getBarridoPantalla().getPosicionBarrido()));
                break;
        }
        return true;
    }

    @Override
    public boolean makeTertiaryClick(MotionEvent event) {
        phrasesView.onClick(phrasesView.getBarridoPantalla().getmListadoVistas().get(phrasesView.getBarridoPantalla().getPosicionBarrido()));
        return true;
    }
}
