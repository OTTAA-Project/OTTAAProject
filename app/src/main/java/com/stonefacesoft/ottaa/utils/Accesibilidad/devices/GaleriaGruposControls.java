package com.stonefacesoft.ottaa.utils.Accesibilidad.devices;

import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;

import com.stonefacesoft.ottaa.GaleriaGrupos2;
import com.stonefacesoft.ottaa.Views.GroupGalleryNavigator;

import androidx.core.view.MotionEventCompat;

public class GaleriaGruposControls extends Controls {
    private final GroupGalleryNavigator galeriaGrupos;
    private final String TAG="GaleriaGroupsControls";
    public GaleriaGruposControls(GroupGalleryNavigator galeriaGrupos2) {
        super(galeriaGrupos2);
        this.galeriaGrupos=galeriaGrupos2;
    }

    @Override
    public boolean makeClick(MotionEvent event) {
        if (event.getSource() == InputDevice.SOURCE_MOUSE&&(galeriaGrupos.getBarridoPantalla().isAvanzarYAceptar()||galeriaGrupos.getBarridoPantalla().isSipAndPuff())) {
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

        }else if(event.getSource()==InputDevice.SOURCE_MOUSE&&galeriaGrupos.getBarridoPantalla().isScrollModeClicker()){
            switch (event.getButtonState()){
                case  MotionEvent.BUTTON_PRIMARY:
                    galeriaGrupos.OnClickBarrido();
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
        galeriaGrupos.getBarridoPantalla().avanzarBarrido();
        switch (preferences.getInt("deviceId",0)){
            case 0:
                if(galeriaGrupos.getBarridoPantalla().isSipAndPuff())
                    galeriaGrupos.getFunction_scroll().HacerClickEnTiempo();
                break;
        }
        return true;
    }

    @Override
    public boolean makeSecondaryClick(MotionEvent event) {
        switch (preferences.getInt("deviceId",0)){
            case 0:
                galeriaGrupos.getBarridoPantalla().volverAtrasBarrido();
                if(galeriaGrupos.getBarridoPantalla().isSipAndPuff())
                    galeriaGrupos.getFunction_scroll().HacerClickEnTiempo();
                break;
            case 1:
                galeriaGrupos.onClick(galeriaGrupos.getBarridoPantalla().getmListadoVistas().get(galeriaGrupos.getBarridoPantalla().getPosicionBarrido()));
                break;
        }
        return true;
    }

    @Override
    public boolean makeTertiaryClick(MotionEvent event) {
        galeriaGrupos.onClick(galeriaGrupos.getBarridoPantalla().getmListadoVistas().get(galeriaGrupos.getBarridoPantalla().getPosicionBarrido()));
        return true;
    }

    @Override
    public boolean pressBackButton() {
        galeriaGrupos.getBarridoPantalla().getmListadoVistas().get(galeriaGrupos.getBarridoPantalla().getPosicionBarrido()).callOnClick();
        return true;
    }
}
